SELECT DISTINCT
    c.nombre || ' ' || c.apellidos as nombre_cliente
FROM
    BTG.cliente c
        INNER JOIN
    BTG.inscripcion i ON c.id = i.idCliente
        INNER JOIN
    BTG.visitan v ON c.id = v.idCliente
        INNER JOIN
    BTG.disponibilidad d ON i.idProducto = d.idProducto AND v.idSucursal = d.idSucursal;