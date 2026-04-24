/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.service;

import com.tienda.domain.Categoria;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tienda.repository.CategoriaRepository;


@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Transactional(readOnly=true)
    public List<Categoria> getCategorias(boolean activos){
        var lista=categoriaRepository.findAll();
        
        if (activos) {
            lista.removeIf(e -> !e.isActivo());
        }
        
        return lista;
        
    }
    
    @Transactional
    public void save(Categoria categoria){
       categoriaRepository.save(categoria);
        }
}
