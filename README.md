[![License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

## Micronaut POC using GraalVM

This proof of concept (POC) demonstrates how to create a Lambda function with [GraalVM](https://www.graalvm.org/), DynamoDB using Micronaut. To achieve this, you will need Amazon API Gateway.

## Component AWS Configuration
- [API Gateway](https://aws.amazon.com/es/api-gateway/)
- [Lambda](https://aws.amazon.com/es/lambda/): 
  - Architecture: x86_64
  - Runtime: Custom runtime on Amazon Linux 2
  - Handler: io.micronaut.function.aws.proxy.MicronautLambdaHandler
- [DynamoDB](https://aws.amazon.com/dynamodb/?nc1=h_ls)
---
## Build

To deploy you application you can generated using: ```./gradlew buildNativeLambda``` (This step can take some time because is gonna use an image of GraalVM in Docker).

The file generated is under ```/build/libs/poc-0.1-lambda.zip```You can go to you lambda function and upload the file in AWS.

---
## Testing
### Remote
  - Requisites:
    - A IAM role in AWS with permissions of:
      - Lambda Execute
      - Read && Write in DynamoDB
      - (Optional) Read && Write CloudWatch to see logs
      - Create a Lambda function in AWS and upload the zip generated in the build process
      - Create an API Gateway and connect with the lambda function using ```{+proxy}``` mode
      - Test using ```/counter```in API Gateway and deploy you API. 
### Local
  - Requisites:
    - Gradle 8.2.1
    - Java 17
    - Docker
    - Executes for unit test:
      - ```git clone https://github.com/denjossal/simple-micronaut-app.git```
      - ```cd simple-micronaut-app```
      - ```./gradlew test```
    - (Optional) check the enviroment variable 'AWS_REGION'


<!-- MARKDOWN LINKS & IMAGES -->
[license-shield]: https://img.shields.io/badge/License-Apache_2.0-blue.svg?style=for-the-badge
[license-url]: https://www.apache.org/licenses/LICENSE-2.0
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/djsalcedo