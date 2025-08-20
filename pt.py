import os

# Lista de tablas/carpeta agregados (ajusta si agregas más)
agregados = [
    "categorias",
    "clientes",
    "compras",
    "detalle_compras",
    "detalle_ventas",
    "empleados",
    "productos",
    "proveedores",
    "ventas"
]

# Paquete base según tu estructura de proyecto
pkg_base = "src/main/java/com/solvegrades/farma"

# Subcarpetas DDD por agregado
subcarpetas = [
    "application/dto",
    "application/services",
    "domain/entities",
    "domain/repositories",
    "infrastructure/model",
    "infrastructure/repositories",
    "presentation/controllers",
    "presentation/routes"
]

for agregado in agregados:
    for sub in subcarpetas:
        path = os.path.join(pkg_base, agregado, sub)
        os.makedirs(path, exist_ok=True)
        print(f"Directorio creado: {path}")

print("¡Todas las subcarpetas DDD se han creado correctamente!")