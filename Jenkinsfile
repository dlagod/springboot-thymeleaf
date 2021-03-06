// Lista de perfiles
List axes = ['h2','mysql','postgres','oracle']


pipeline {

    // Nodo donde se ejecuta la tarea
    agent {
        node {
            label 'master'
			customWorkspace "workspace/MultiBranch/${BRANCH_NAME}"
        }
    }
  
    // Opciones de configuración del proyecto
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        disableConcurrentBuilds()
    }
    
    // Definición de las variables de entorno
    environment {
        LC_ALL="es_ES.UTF-8"
        LANG="es_ES.UTF-8"
    }
    
    //Herramientas
    tools { 
        maven 'MAVEN-3.6.3'
        ant 'ANT-1.9.15'
        jdk 'jdk1.8.0_111'
    }
  
    // Parametrización de la Build
    parameters {
        choice(name: 'PROFILE', choices: ['all'] + axes.each {println "$it"} , description: 'Perfiles del BBDD')
        booleanParam(name: 'TESTS', defaultValue: true, description: 'Paso de las pruebas')
        booleanParam(name: 'QUALITY', defaultValue: true, description: 'Paso de las métricas de calidad')
        booleanParam(name: 'DEPLOY', defaultValue: false, description: 'Deploy en el entorno de preproducción: CI')
    }

    // Estados de la Tarea
    stages {
     
        // CheckOut Git  
        stage('CheckOut') {
            steps {
                echo "checkout"
                
                //Borrar el workSpace    
                cleanWs()
                
                checkout([$class: 'GitSCM', 
                    branches: [[name: "develop"]],
                    browser: [
                        $class: 'GitWeb',
                        repoUrl: "https://github.com/dlagod/springboot-thymeleaf.git"
                    ],
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [], 
                    gitTool: 'Default', 
                    submoduleCfg: [], 
                        userRemoteConfigs: [[credentialsId: 'GitHub', url: 'https://github.com/dlagod/springboot-thymeleaf.git']]
                    ])
            }
        }
    
        // Compilación
        stage('Build') {
            steps {
                // Get groovy Script
                script {
                    
                    // Devuelve las variables de Entorno
                    //echo "workspace: ${WORKSPACE}"
                    //echo "jenkinsHome: ${JENKINS_HOME}"
                    //echo "jobname: ${JOB_NAME}"
        
                    def jobName = "${JOB_NAME}"
                    def job = jobName.split('/');
                    def outputDir = "${JENKINS_HOME}/workspace/"  + job[0] + "/" + job[1] + "/target";
                    
                    //echo "${outputDir}"
      
                    // Se saca la versión del pom.xml
                    def pomFile = "${JENKINS_HOME}/workspace/"  + job[0] + "/" + job[1] + "/pom.xml";
                    
                    // Se saca la versión del pom.xml
                    pom = readMavenPom file: pomFile;
                    def version = pom.version.toString();
                    //echo "Version: " + version;
                    
                    env.VERSION = version
                }
                //load "${WORKSPACE}/target/config.properties"
                buildDescription "Nodo @${NODE_NAME} - v${VERSION}"
                //echo "M2_HOME = ${M2_HOME}"
                echo "Compilando el proyecto - PATH: $PATH"
        
                script {
                    if (env.PROFILE == 'all') {
                        echo 'Compilando todos los perfiles'
                        axes.each {
                            echo "Perfil: $it"
                            bat "mvn -U clean package -P $it -DskipTests"
                        }
                    } else {
                        echo "compilando el perfil: ${PROFILE}"
                        bat "mvn -U clean package -P ${PROFILE} -DskipTests"
                    }
                }
            }
        }
        
        // Pruebas
        stage('Tests') {
            when {
                // Only TEST equals True
                expression {return params.TESTS ==~ /true/}    
            } 
            steps {
                echo "Ejecutando el paso de las pruebas"
                script {
                    
                    if (env.PROFILE == 'all') {
                        def perfil = axes.first();
                        bat "mvn -U org.jacoco:jacoco-maven-plugin:prepare-agent test org.jacoco:jacoco-maven-plugin:report -P ${perfil} -Dmaven.test.failure.ignore=true"
                    } else {
                        bat "mvn -U org.jacoco:jacoco-maven-plugin:prepare-agent test org.jacoco:jacoco-maven-plugin:report -P ${PROFILE} -Dmaven.test.failure.ignore=true"
                    }
                }
            }    
        }
        
        // Informes Reports  
        stage('Code Quality') {
            when {
                // Only QUALITY equals True
                expression {return params.QUALITY ==~ /true/}    
            } 
            steps {
                // Con el plugin de SonarQube Scanner
                withSonarQubeEnv('SonarQube') {
                    bat label: 'Ejecutando SonarQube', script: 'mvn sonar:sonar -Dsonar.links.ci="%JOB_URL%" -Dsonar.links.scm="https://github.com/dlagod/springboot-thymeleaf.git" -Dsonar.links.scm_dev="https://github.com/dlagod/springboot-thymeleaf.git"'
                }
                
                // Se espera a que termine elproceso y se continua la pipeline si el
                // resultado QUALITY GATE es Passed 
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        // Se sube a Nexus 
        stage('Upload') {
            // Se comprobaría si se pasan las metricas de calidad
            steps {
                    script {
                    if (env.PROFILE == 'all') {
                        def perfil = axes.first();
                       bat "mvn jar:jar deploy:deploy -P ${perfil} -DskipTests"
                    } else {
                        bat "mvn jar:jar deploy:deploy -P ${PROFILE} -DskipTests"
                    }
                }
            }
        }
        
        // Depliegue
        stage('Deploy') {
            when {
                // Only DEPLOY equals True
                expression {return params.DEPLOY ==~ /true/} 
                //branch 'develop'
            } 
            steps {
            	
                echo "Desplegando en el entorno de  PREPRODUCCIÓN: CI"
                /*
                // Incompatible con Windows ssh-agent
                sshagent(['CI']) {
                    bat  "ssh -o StrictHostKeyChecking=no IEUser@CI dir"
                }
                */
                
                // Control de Errores
                catchError(buildResult: "FAILURE", stageResult: "FAILURE") {
                    
                    // Se publica el artefacto
                    sshPublisher(
                        continueOnError: false, failOnError: true,
                        publishers: [
                            sshPublisherDesc(
                                configName: "CI",
                                verbose: true,
                                transfers: [
                                    sshTransfer(
                                        sourceFiles: "target/*-SNAPSHOT.jar",
                                        removePrefix: "target",
                                        remoteDirectory: "springboot-thymeleaf",
                                        //execCommand: "javaw -jar \"..\\..\\springboot-thymeleaf\\springboot-thymeleaf-1.0.0-SNAPSHOT.jar\""
                                        execCommand: "start /B java -jar \"..\\..\\springboot-thymeleaf\\springboot-thymeleaf-1.0.0-SNAPSHOT.jar\""
                                        //execCommand: "dir \"..\\..\\springboot-thymeleaf\""
                                        //execCommand: "echo \"$VERSION\""
                                    )
                                ]
                            )
                        ]
                    )
                }
            }
        }
        
        // Pruebas de Acceptacion
        stage('Acceptance Test') {
            //when {expression { equals expected: "SUCCESS", actual: currentBuild.currentResult } }
            when { expression { return currentBuild.currentResult == "SUCCESS" } }
            steps {
                // Se ejecutarían las pruebas funcionales automatizadas: Selenium, Appium
                // Se ejecutan las pruebas de integración Postman, SOAPUI
                // Se ejecutarían pruebas de Rendimiento Jmeter
                echo "Pipeline result: ${currentBuild.result}"
                echo "Pipeline currentResult: ${currentBuild.currentResult}"
                echo "Se pasan las pruebas de Aceptación"
                sleep 90 // seconds
                powershell 'wget http://localhost:8082/tabla/list'
            }
        }
    }
    
    
    // Postacciones
    post {
    
        // Estas acciones se realizan siempre
        always {
            
            //Publicacion Artefactos
            //archiveArtifacts artifacts: 'artifacts/*.war', fingerprint: true
            
            // Se muestran los resultados de JUnit
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/**/*.xml' 
            
            // Envío de EMAIL
            emailext(
                subject: '$DEFAULT_SUBJECT', 
                to: '$DEFAULT_RECIPIENTS', 
                replyTo: '$DEFAULT_REPLYTO', 
                body: '$DEFAULT_CONTENT', 
                mimeType: 'text/html',
                recipientProviders: [brokenTestsSuspects(), brokenBuildSuspects(), developers()],
                attachmentsPattern: 'target/*.docx, target/*.xlsx')
        }
    }
}