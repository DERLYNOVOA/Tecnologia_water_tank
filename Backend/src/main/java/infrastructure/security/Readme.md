## SimplePasswordHasher.java


Implementa el contrato PasswordHasher del dominio. Vive en infrastructure/security porque SHA-256 es un detalle de implementación de seguridad, no lógica de negocio. El dominio solo sabe que existe algo que hashea contraseñas, no sabe que es SHA-256.
hashPassword toma el texto plano, lo pasa por el algoritmo SHA-256 que produce 32 bytes, y los convierte a Base64 para tener un string legible y guardable. El resultado siempre es el mismo para la misma entrada, lo que permite verificar después.
verifyPassword no guarda nada ni compara en reversa (SHA-256 no es reversible). Lo que hace es hashear de nuevo la contraseña que el usuario escribió y comparar ese nuevo hash con el hash guardado. Si son iguales, la contraseña es correcta.
Problema activo que se mencionó antes: Credential tiene un campo salt que nunca se usa aquí. El salt debería concatenarse a la contraseña antes de hashear para que dos usuarios con la misma contraseña tengan hashes distintos. Para el proyecto académico está bien así, pero es importante saberlo.
