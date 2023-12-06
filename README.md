# globallogic_test
Ejercicio Java API Usuarios_GlobalLogic para cliente BCI

EndPoints:

/signup
URL: http://localhost:8080/user/signup
Petición Http: POST
Return: Datos del Usuario creado.
SignUp (Registro de un nuevo Usuario)

Json en Body de ingreso:
{
  "name": String,
  "email": String ("email@servidor.algo"),
  "password": String ("a2asfGfdfdf4"),
  "phones": [
    {
      "number": long (12345678),
      "citycode": int (9),
      "countrycode": String ("+56")
    }
  ]
}

/login
URL: http://localhost:8080/user/login
Petición Http: POST
Return: Datos del Usuario.
Login (Regresa los datos del Usuario según el Token entregado)

Authorization:
Bearer Token: "Bearer [token_generado_en_función_SignUp]"
