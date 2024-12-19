
package com.CRUD.Empleado.repository;

import com.CRUD.Empleado.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    // Métodos personalizados si son necesarios
}