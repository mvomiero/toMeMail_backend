# **Terraform Deployment Guide**

## **What is Terraform?**
Terraform is an **Infrastructure as Code (IaC)** tool that allows you to define, provision, and manage cloud resources **declaratively**. Instead of manually creating cloud resources, Terraform lets you define them in a configuration file, ensuring **consistency**, **scalability**, and **easy updates**.

### **Why use Terraform?**
- **Automates cloud resource creation** instead of using the UI.
- **Keeps infrastructure consistent** by versioning configurations.
- **Easily updates and scales resources** by modifying the configuration files and applying changes.
- **Supports multiple cloud providers** (Azure, AWS, GCP, etc.).

---

## **Terraform Structure in This Project**
In this project, Terraform is used to deploy the **toMeMail backend** to **Azure**. The Terraform setup follows a structured approach:

1. **`main.tf`** – Defines the infrastructure (Azure App Service, SQL database, resource group, etc.).
2. **`variables.tf`** – Specifies the required **input variables** for the infrastructure (e.g., database name, app name).
3. **`terraform.tfvars`** – Stores the actual values for the variables **(this file is not committed for security reasons)**.

> 💡 **Why separate variables?** This structure allows better **reusability** and **security**, ensuring sensitive values (like database credentials) are stored securely.

---

## **Terraform and Azure Authentication**
To interact with Azure, Terraform needs to authenticate. This is done using the **Azure CLI**:
1. **Log in to Azure**:
   ```sh
   az login
   ```
   This opens a browser window for authentication.

2. **Set the correct subscription (if needed)**:
   ```sh
   az account set --subscription <subscription_id>
   ```
   Your **subscription ID** is used in the `provider "azurerm"` block.

3. **Terraform automatically uses this login session** to deploy resources.

---

## **Terraform Commands**
Once Terraform is set up, you can deploy infrastructure using the following steps:

### **1️⃣ Initialize Terraform**
Run this in the `terraform/` directory:
```sh
terraform init
```
- Downloads required **providers** (e.g., `azurerm` for Azure).
- Sets up the working directory for Terraform.

### **2️⃣ Plan the Changes**
```sh
terraform plan
```
- Shows what Terraform **will create/update/delete**.
- Ensures there are no syntax or configuration errors.

### **3️⃣ Apply the Configuration**
```sh
terraform apply
```
- Deploys the defined resources to **Azure**.
- Terraform prompts for confirmation before making changes.

> **Note**: To apply without confirmation, use:
> ```sh
> terraform apply -auto-approve
> ```

### **4️⃣ Destroy Resources (Optional)**
To delete all resources managed by Terraform:
```sh
terraform destroy
```
> **Use with caution!** This **removes all deployed resources**.

---

## **How Terraform Interacts with Azure**
Terraform **interacts with Azure** using the Azure Resource Manager (ARM) API. The **Azure Provider (`azurerm`)** is configured with:
- **Subscription ID** – Identifies the Azure account.
- **Resource Groups** – Organizes resources within Azure.
- **App Services** – Deploys the backend API.
- **Database Configuration** – Creates an Azure SQL database and firewall rules.

---
