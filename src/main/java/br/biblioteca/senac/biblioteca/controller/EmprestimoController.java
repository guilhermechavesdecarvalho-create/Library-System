package br.biblioteca.senac.biblioteca.controller;

import br.biblioteca.senac.biblioteca.model.Emprestimo;
import br.biblioteca.senac.biblioteca.service.EmprestimoService;

import java.sql.SQLException;
import java.util.List;

public class EmprestimoController {

    private EmprestimoService emprestimoService;

    public EmprestimoController() {
        this.emprestimoService = new EmprestimoService();
    }

    public void realizarEmprestimo(Emprestimo emprestimo) {
        try {
            emprestimoService.realizarEmprestimo(emprestimo);
            System.out.println("Sucesso: Empréstimo realizado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados ao realizar empréstimo: " + e.getMessage());
        }
    }

    public void realizarDevolucao(Long emprestimoId) {
        try {
            emprestimoService.realizarDevolucao(emprestimoId);
            System.out.println("Sucesso: Devolução registrada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados ao registrar devolução: " + e.getMessage());
        }
    }

    public List<Emprestimo> listarTodos() {
        try {
            return emprestimoService.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar os empréstimos: " + e.getMessage());
            return null;
        }
    }

    public Emprestimo buscarPorId(Long id) {
        try {
            return emprestimoService.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar o empréstimo por ID: " + e.getMessage());
            return null;
        }
    }
}