# **Cloud Deployment with Azure and Terraform**

## **Overview**
The **toMeMail** backend is deployed on **Microsoft Azure** using **Terraform** for infrastructure as code (IaC). This approach ensures **reproducibility**, **scalability**, and **automation** for provisioning cloud resources.

## **Infrastructure Overview**
The following **Azure resources** are provisioned using **Terraform**:
- **Resource Group**: Groups all resources under one logical container.
- **Azure App Service Plan**: Defines the compute resources for the backend deployment.
- **Azure Web App (Linux)**: Hosts the **toMeMail** backend API.
- **Azure SQL Server & Database**: Stores application data.
- **Firewall Rules**: Controls database access.
- **Static Web App**: Reserved for future frontend deployment.

[Terraform Configuration](https://github.com/mvomiero/toMeMail_backend/blob/main/terraform/main.tf)
[Terraform Configuration](https://github.com/mvomiero/toMeMail_backend/blob/docs/terraform-deployment.md)

## **Profiles for Deployment**
The backend uses **two profiles** for managing different environments:

1. **Development (`application.properties`)**
    - Uses an **H2 in-memory database** for local development.
    - Automatically loads **predefined schemas and sample data** to facilitate testing.

2. **Deployment (`application-azure.properties`)**
    - Connects to an **Azure SQL database**.
    - The **connection string, username, and password** are provided as **Terraform variables**.
    - These credentials are then **stored as Azure environment variables** for security.
    - The **backend runs on port `80`** instead of the default `8080`, as required by **Azure App Service**.

[Spring Boot Profiles](https://github.com/mvomiero/toMeMail_backend/tree/main/src/main/resources)

During deployment, the **Spring profile** is set via:
```hcl
"SPRING_PROFILES_ACTIVE" = "azure"
```
This ensures that the correct environment-specific configurations are loaded.

## **Automated CI/CD Deployment with GitHub Actions**
A **GitHub Actions workflow** automates deployment by:
1. **Building** the Spring Boot application using Maven.
2. **Uploading** the artifact (JAR file).
3. **Authenticating** with Azure using secrets.
4. **Deploying** the JAR to **Azure App Service**.

[GitHub Actions Deployment Workflow](https://github.com/mvomiero/toMeMail_backend/blob/main/.github/workflows/deploy-backend.yml)

## **Manual Deployment to Azure**
It is also possible to **deploy manually** using the Azure CLI:
1. **Package the Spring Boot application**:
   ```sh
   mvn clean package -DskipTests
   ```
2. **Deploy the JAR manually**:
   ```sh
   az webapp deploy --resource-group toMeMailResourceGroup \
                    --name tome-mail-backend \
                    --src-path ./target/*.jar
   ```
This provides flexibility for testing or troubleshooting outside of the CI/CD pipeline.
