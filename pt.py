import os

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

pkg_base = "com.solvegrades.farma"
src_root = "src/main/java/" + pkg_base.replace('.', '/')

plantillas = {
    "domain/entities/{Entity}.java":
        "package {base}.{agregado}.domain.entities;\n\npublic class {Entity} {{\n\n}}",
    "domain/repositories/{Entity}Repository.java":
        "package {base}.{agregado}.domain.repositories;\n\npublic interface {Entity}Repository {{\n\n}}",
    "application/dto/{Entity}DTO.java":
        "package {base}.{agregado}.application.dto;\n\npublic class {Entity}DTO {{\n\n}}",
    "application/services/{Entity}Service.java":
        "package {base}.{agregado}.application.services;\n\npublic class {Entity}Service {{\n\n}}",
    "presentation/controllers/{Entity}Controller.java":
        "package {base}.{agregado}.presentation.controllers;\n\npublic class {Entity}Controller {{\n\n}}",
}

def to_camel_case(s):
    return ''.join(x.capitalize() for x in s.split('_'))

for agregado in agregados:
    Entity = to_camel_case(agregado)
    for plantilla_path, contenido in plantillas.items():
        rel_path = plantilla_path.format(Entity=Entity)
        abs_path = os.path.join(src_root, agregado, rel_path)
        dir_path = os.path.dirname(abs_path)
        os.makedirs(dir_path, exist_ok=True)
        java_code = contenido.format(base=pkg_base, agregado=agregado, Entity=Entity)
        with open(abs_path, "w", encoding="utf-8") as f:
            f.write(java_code)
        print(f"Archivo creado: {abs_path}")

print("¡Archivos Java DDD creados con éxito!")