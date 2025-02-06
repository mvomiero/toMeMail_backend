variable "resource_group_name" {
  description = "Name of the Azure Resource Group"
  type        = string
  default     = "toMeMailResourceGroup"
}

variable "location" {
  description = "Azure Region"
  type        = string
  default     = "West Europe"
}

variable "app_service_plan_name" {
  description = "Name of the App Service Plan"
  type        = string
  default     = "tome-mail-backend-plan"
}

variable "webapp_name" {
  description = "Name of the Azure App Service for the backend"
  type        = string
  default     = "tome-mail-backend"
}

variable "database_server_name" {
  description = "Azure SQL Server Name"
  type        = string
  default     = "tome-mail-sqlserver"
}

variable "database_name" {
  description = "Azure SQL Database Name"
  type        = string
  default     = "tomeMailDB"
}

variable "database_username" {
  description = "Admin username for the database"
  type        = string
}

variable "database_password" {
  description = "Admin password for the database"
  type        = string
  sensitive   = true
}

variable "secret_key" {
  description = "Secret key for the application"
  type        = string
  sensitive   = true
}
