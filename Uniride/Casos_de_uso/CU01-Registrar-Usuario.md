# CU01 - Registrar Usuario

**Actor:** Estudiante (nuevo usuario)

**Objetivo:** Permitir que un estudiante cree una cuenta en el sistema UniRide.

## Guion (Curso normal de eventos)

1. El actor accede a la pantalla de registro.
2. El sistema solicita correo institucional, nombre completo, contraseña, universidad y carrera.
3. El actor ingresa su correo institucional.
4. El sistema verifica que no exista otro usuario con ese correo.
5. El actor ingresa su nombre completo.
6. El actor ingresa su contraseña.
7. El sistema valida que la contraseña tenga al menos 8 caracteres, un número y una mayúscula.
8. El actor selecciona si será Conductor, Pasajero o Ambos.
9. El sistema crea el nuevo usuario.
10. El sistema envía correo de confirmación.

## Excepciones

### **E1 – El correo ya está registrado**
1. El sistema muestra: "Ya existe una cuenta con este correo institucional".
2. El sistema sugiere recuperación de contraseña.
3. Termina el caso.

### **E2 – Contraseña no válida**
1. El sistema muestra: "La contraseña debe tener mínimo 8 caracteres, incluir un número y una letra mayúscula".
2. El sistema mantiene los datos ingresados excepto la contraseña.
3. El actor puede corregirla.

## Postcondiciones
- Usuario registrado en el sistema.
- Perfil creado según su rol elegido.