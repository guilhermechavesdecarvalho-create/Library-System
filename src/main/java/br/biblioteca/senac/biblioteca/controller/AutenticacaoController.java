package br.biblioteca.senac.biblioteca.controller;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.sql.*;

@Controller
public class AutenticacaoController {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro_usuario";
    }

    @GetMapping("/recuperar-senha")
    public String recuperar() {
        return "recuperar_senha";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session,
            RedirectAttributes flash) {
        String sql = "SELECT id,nome,senha_hash FROM usuario WHERE email=?";
        try (Connection c = Conexao.conectar(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email.trim().toLowerCase());
            try (ResultSet r = p.executeQuery()) {
                if (r.next() && r.getString("senha_hash") != null
                        && encoder.matches(senha, r.getString("senha_hash"))) {
                    session.setAttribute("usuarioId", r.getLong("id"));
                    session.setAttribute("usuarioNome", r.getString("nome"));
                    return "redirect:/painel";
                }
            }
            flash.addFlashAttribute("erro", "E-mail ou senha inválidos.");
            return "redirect:/";
        } catch (SQLException e) {
            flash.addFlashAttribute("erro",
                    "Não foi possível conectar ao banco. Confira a configuração e execute o script SQL.");
            return "redirect:/";
        }
    }

    @PostMapping("/cadastro")
    public String cadastrar(@RequestParam String nome, @RequestParam String cpf, @RequestParam String email,
            @RequestParam(required = false) String telefone, @RequestParam String senha, RedirectAttributes flash) {
        String cpfLimpo = cpf.replaceAll("\\D", "");
        if (nome.isBlank() || cpfLimpo.length() != 11 || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
                || senha.length() < 8) {
            flash.addFlashAttribute("erro",
                    "Confira os dados. Informe CPF com 11 dígitos, e-mail válido e senha com pelo menos 8 caracteres.");
            return "redirect:/cadastro";
        }
        String sql = "INSERT INTO usuario(nome,cpf,email,telefone,senha_hash) VALUES(?,?,?,?,?)";
        try (Connection c = Conexao.conectar(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, nome.trim());
            p.setString(2, cpfLimpo);
            p.setString(3, email.trim().toLowerCase());
            p.setString(4, telefone == null ? "" : telefone.trim());
            p.setString(5, encoder.encode(senha));
            p.executeUpdate();
            flash.addFlashAttribute("sucesso", "Conta criada. Agora você já pode entrar no sistema.");
            return "redirect:/";
        } catch (SQLException e) {
            flash.addFlashAttribute("erro",
                    e.getSQLState() != null && e.getSQLState().startsWith("23")
                            ? "Este CPF ou e-mail já está cadastrado."
                            : "Não foi possível cadastrar. Verifique a conexão com o banco.");
            return "redirect:/cadastro";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/recuperar-senha")
    public String recuperarSenha(@RequestParam String email, RedirectAttributes flash) {
        // Não existe serviço de e-mail/token configurado; não afirmar que um e-mail foi
        // enviado.
        flash.addFlashAttribute("erro",
                "A recuperação automática ainda não está configurada. Solicite a redefinição ao responsável pelo sistema.");
        return "redirect:/recuperar-senha";
    }
}
