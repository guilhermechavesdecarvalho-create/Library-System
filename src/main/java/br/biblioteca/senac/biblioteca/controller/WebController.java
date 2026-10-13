package br.biblioteca.senac.biblioteca.controller;

import br.biblioteca.senac.biblioteca.dao.*;
import br.biblioteca.senac.biblioteca.model.*;
import br.biblioteca.senac.biblioteca.service.EmprestimoService;
import br.biblioteca.senac.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.SQLException;
import java.time.LocalDate;

@Controller
public class WebController {

    private final LivroDao livros = new LivroDaoMySQLImpl();
    private final UsuarioDao usuarios = new UsuarioDaoMySQLImpl();
    private final EmprestimoService emprestimos = new EmprestimoService();
    private final UsuarioService usuarioService = new UsuarioService();

    @GetMapping({ "/", "/login" })
    public String login() {
        return "index";
    }

    @GetMapping("/painel")
    public String painel(Model model) {
        try {
            model.addAttribute("totalLivros", livros.listarTodos().size());
            model.addAttribute("totalUsuarios", usuarios.listarTodos().size());
            var todos = emprestimos.listarTodos();
            model.addAttribute("emprestimosAtivos", todos.stream().filter(Emprestimo::isStatus).count());
            model.addAttribute("emprestimosRecentes", todos.stream().limit(5).toList());
        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível carregar os dados. Verifique a conexão com o banco.");
        }
        return "painel";
    }

    @GetMapping("/livros")
    public String listarLivros(@RequestParam(required = false) String busca, Model model) {
        try {
            var lista = livros.listarTodos();
            if (busca != null && !busca.isBlank()) {
                String q = busca.toLowerCase();
                lista = lista.stream()
                        .filter(l -> l.getTitulo().toLowerCase().contains(q)
                                || l.getAutor().getNome().toLowerCase().contains(q)
                                || l.getIsbn().toLowerCase().contains(q))
                        .toList();
            }
            model.addAttribute("livros", lista);
            model.addAttribute("busca", busca == null ? "" : busca);
        } catch (Exception e) {
            model.addAttribute("livros", java.util.List.of());
            model.addAttribute("erro", "Não foi possível carregar o acervo. Verifique o banco de dados.");
        }
        return "livros";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(required = false) String busca, Model model) {
        try {
            var lista = usuarios.listarTodos();
            if (busca != null && !busca.isBlank()) {
                String q = busca.toLowerCase();
                lista = lista.stream().filter(u -> u.getNome().toLowerCase().contains(q)
                        || u.getEmail().toLowerCase().contains(q) || u.getCPF().contains(q)).toList();
            }
            model.addAttribute("usuarios", lista);
            model.addAttribute("busca", busca == null ? "" : busca);
        } catch (Exception e) {
            model.addAttribute("usuarios", java.util.List.of());
            model.addAttribute("erro", "Não foi possível carregar os usuários. Verifique a conexão com o banco.");
        }
        return "usuarios";
    }

    @PostMapping("/usuarios/{id}/atualizar")
    public String atualizarUsuario(@PathVariable Long id, @RequestParam String nome, @RequestParam String email,
            @RequestParam(required = false) String telefone, RedirectAttributes flash) {
        try {
            Usuario u = usuarios.buscarPorId(id);
            if (u == null)
                throw new IllegalArgumentException("Usuário não encontrado.");
            u.setNome(nome.trim());
            u.setEmail(email.trim().toLowerCase());
            u.setTelefone(telefone == null ? "" : telefone.trim());
            usuarioService.atualizarUsuario(u);
            flash.addFlashAttribute("sucesso", "Dados do usuário atualizados.");
        } catch (Exception e) {
            flash.addFlashAttribute("erro",
                    e.getMessage() == null ? "Não foi possível atualizar o usuário." : e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/{id}/excluir")
    public String excluirUsuario(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioService.excluirUsuario(id);
            flash.addFlashAttribute("sucesso", "Usuário excluído.");
        } catch (Exception e) {
            flash.addFlashAttribute("erro",
                    "Não foi possível excluir. O usuário pode possuir empréstimos registrados.");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/emprestimos")
    public String formularioEmprestimo(Model model) {
        carregarOpcoes(model);
        model.addAttribute("emprestimo", new Emprestimo());
        return "emprestimo";
    }

    @PostMapping("/emprestimos")
    public String emprestar(@RequestParam Long livroId, @RequestParam Long usuarioId, RedirectAttributes flash) {
        try {
            Emprestimo e = new Emprestimo();
            Livro l = new Livro();
            l.setId(livroId);
            Usuario u = new Usuario();
            u.setId(usuarioId);
            e.setLivro(l);
            e.setUsuario(u);
            e.setDataEmprestimo(LocalDate.now());
            emprestimos.realizarEmprestimo(e);
            flash.addFlashAttribute("sucesso",
                    "Empréstimo registrado. Prazo de devolução: " + e.getDataDevolucao() + ".");
            return "redirect:/emprestimos/lista";
        } catch (Exception e) {
            flash.addFlashAttribute("erro",
                    e.getMessage() == null ? "Não foi possível realizar o empréstimo." : e.getMessage());
            return "redirect:/emprestimos";
        }
    }

    @GetMapping("/emprestimos/lista")
    public String listarEmprestimos(Model model) {
        try {
            model.addAttribute("emprestimos", emprestimos.listarTodos());
        } catch (Exception e) {
            model.addAttribute("emprestimos", java.util.List.of());
            model.addAttribute("erro", "Não foi possível carregar os empréstimos.");
        }
        return "devolucao";
    }

    @PostMapping("/emprestimos/{id}/devolver")
    public String devolver(@PathVariable Long id, RedirectAttributes flash) {
        try {
            emprestimos.realizarDevolucao(id);
            flash.addFlashAttribute("sucesso", "Devolução registrada com sucesso.");
        } catch (Exception e) {
            flash.addFlashAttribute("erro",
                    e.getMessage() == null ? "Não foi possível registrar a devolução." : e.getMessage());
        }
        return "redirect:/emprestimos/lista";
    }

    @GetMapping("/devolucoes")
    public String devolucoes() {
        return "redirect:/emprestimos/lista";
    }

    private void carregarOpcoes(Model model) {
        try {
            model.addAttribute("livros",
                    livros.listarTodos().stream().filter(l -> l.getQuantidadeDisponivel() > 0).toList());
            model.addAttribute("usuarios", usuarios.listarTodos());
        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível carregar livros e usuários. Verifique o banco de dados.");
            model.addAttribute("livros", java.util.List.of());
            model.addAttribute("usuarios", java.util.List.of());
        }
    }
}
