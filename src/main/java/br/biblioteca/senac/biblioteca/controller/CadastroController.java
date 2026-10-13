package br.biblioteca.senac.biblioteca.controller;

import br.biblioteca.senac.biblioteca.dao.LivroDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.model.Autor;
import br.biblioteca.senac.biblioteca.model.Livro;
import br.biblioteca.senac.biblioteca.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CadastroController {
    private final LivroService service = new LivroService(new LivroDaoMySQLImpl());

    @GetMapping({ "/livros/cadastrar", "/cadastrar" })
    public String formulario(Model model) {
        model.addAttribute("livro", new Livro());
        return "cadastrar";
    }

    @PostMapping("/livros/cadastrar")
    public String cadastrar(@RequestParam String titulo, @RequestParam String autor,
            @RequestParam(defaultValue = "Brasileira") String nacionalidade,
            @RequestParam int quantidadeTotal, @RequestParam String isbn, @RequestParam String editora,
            RedirectAttributes flash) {
        try {
            if (quantidadeTotal < 1)
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            Livro livro = new Livro(titulo.trim(), new Autor(0, autor.trim(), nacionalidade.trim()), quantidadeTotal,
                    isbn.trim(), editora.trim());
            livro.setQuantidadeDisponivel(quantidadeTotal);
            service.cadastrarLivro(livro);
            flash.addFlashAttribute("sucesso", "Livro cadastrado com sucesso.");
        } catch (Exception e) {
            flash.addFlashAttribute("erro", mensagem(e, "Não foi possível cadastrar o livro."));
            return "redirect:/cadastrar";
        }
        return "redirect:/livros";
    }

    @PostMapping("/livros/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes flash) {
        try {
            new LivroController().excluir(id);
            flash.addFlashAttribute("sucesso", "Livro excluído.");
        } catch (Exception e) {
            flash.addFlashAttribute("erro", mensagem(e, "Não foi possível excluir o livro."));
        }
        return "redirect:/livros";
    }

    private String mensagem(Exception e, String padrao) {
        return e.getMessage() == null ? padrao : e.getMessage();
    }
}
