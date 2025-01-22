# ShoppingCart-Backend
## Database design
### 1. User
| Attribute Name | Type    | Description       | 
|----------------|---------|-------------------|
| id             | Long    | PK, user_id       |
| username       | VARCHAR | username(unique)  |
| password       | VARCHAR | password          |
| email          | VARCHAR | email(unique)     |
| phone          | VARCHAR | phone(unique)     |
| address        | TEXT    | address(unique)   |
| role           | VARCHAR | customer or admin |
| last_name      | VARCHAR | name              |
| first_name     | VARCHAR | name              |
