package org.example.service;

import org.example.DAO.ProfDAO;
import org.example.entity.Prof;

import java.util.List;

public class ProfService {
    private final ProfDAO profDAO;

    public ProfService() {
        this.profDAO = new ProfDAO();
    }

    public List<Prof> getAllProfs() {
        return profDAO.getAllProfs();
    }
}