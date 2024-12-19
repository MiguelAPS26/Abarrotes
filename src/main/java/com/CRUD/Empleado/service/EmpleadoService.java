
package com.CRUD.Empleado.service;

import com.CRUD.Empleado.model.Empleado;
import com.CRUD.Empleado.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }

    public Optional<Empleado> buscarPorId(int id) {
        return empleadoRepository.findById(id);
    }

    public void guardar(Empleado empleado) {
        empleadoRepository.save(empleado);
    }

    public void eliminar(int id) {
        empleadoRepository.deleteById(id);
    }
}
