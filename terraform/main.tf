provider "azurerm" {
  features {}

  subscription_id = "eb725cae-7721-4e42-8e2b-b290d418e5f9"
}

# 🔹 Create Resource Group (Fixing Missing Reference)
resource "azurerm_resource_group" "tome_mail_rg" {
  name     = var.resource_group_name
  location = var.location
}

# 🔹 Create an Azure App Service Plan (Fixing Missing Reference)
resource "azurerm_service_plan" "backend_plan" {
  name                = var.app_service_plan_name
  resource_group_name = azurerm_resource_group.tome_mail_rg.name
  location            = azurerm_resource_group.tome_mail_rg.location
  os_type             = "Linux"
  sku_name            = "F1"  # Free Tier Plan
}

# 🔹 Create Azure Web App (Backend API)
resource "azurerm_linux_web_app" "backend" {
  name                = var.webapp_name
  resource_group_name = azurerm_resource_group.tome_mail_rg.name
  location            = azurerm_resource_group.tome_mail_rg.location
  service_plan_id     = azurerm_service_plan.backend_plan.id


  site_config {
    application_stack {
      java_version         = "21"
      java_server          = "JAVA"
      java_server_version  = "21"


    }
    always_on = false
  }

  app_settings = {
    "DATABASE_URL"         = "jdbc:sqlserver://${var.database_server_name}.database.windows.net:1433;database=${var.database_name};user=${var.database_username}@${var.database_server_name};password=${var.database_password};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
    "DATABASE_USERNAME"    = var.database_username
    "DATABASE_PASSWORD"    = var.database_password
    "SPRING_PROFILES_ACTIVE" = "azure"
    "SECRET_KEY"           = var.secret_key
  }

}

output "backend_url" {
  description = "The URL of the deployed backend application"
  value       = azurerm_linux_web_app.backend.default_hostname
}

# 🔹 Create an Azure SQL Server
resource "azurerm_mssql_server" "tome_mail_sql" {
  name                         = var.database_server_name
  resource_group_name          = azurerm_resource_group.tome_mail_rg.name
  location                     = azurerm_resource_group.tome_mail_rg.location
  version                      = "12.0"
  administrator_login          = var.database_username
  administrator_login_password = var.database_password
}

# 🔹 Create an Azure SQL Database (Fixing Errors)
resource "azurerm_mssql_database" "tome_mail_db" {
  name      = var.database_name
  server_id = azurerm_mssql_server.tome_mail_sql.id

  sku_name = "Basic"
}

resource "azurerm_mssql_firewall_rule" "allow_azure_services" {
  name                = "AllowAzureServices"
  start_ip_address    = "0.0.0.0"
  end_ip_address      = "0.0.0.0"
  server_id           = azurerm_mssql_server.tome_mail_sql.id
}

resource "azurerm_mssql_firewall_rule" "allow_my_ip" {
  name                = "AllowMyIP"
  start_ip_address    = "24.40.135.125"  # Replace with your public IP
  end_ip_address      = "24.40.135.125"  # Replace with your public IP
  server_id           = azurerm_mssql_server.tome_mail_sql.id
}

