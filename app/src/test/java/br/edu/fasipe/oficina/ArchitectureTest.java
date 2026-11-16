package br.edu.fasipe.oficina;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Regras de docs/quality.md: fronteira de pacotes e teto de 350 linhas.
 * O diretório de trabalho dos testes locais é app/.
 */
public class ArchitectureTest {

    private static final Path SOURCE_ROOT = Paths.get("src/main/java/br/edu/fasipe/oficina");
    private static final int MAX_LINES = 350;

    private List<Path> javaFiles(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }
        try (Stream<Path> files = Files.walk(dir)) {
            return files.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList());
        }
    }

    private String read(Path file) throws IOException {
        return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    }

    @Test
    public void sourceRootExists() {
        // Evita que os outros testes passem "vazios" se o caminho estiver errado
        assertTrue("Pasta não encontrada: " + SOURCE_ROOT.toAbsolutePath(), Files.isDirectory(SOURCE_ROOT));
    }

    @Test
    public void uiDoesNotAccessDatabaseDirectly() throws IOException {
        for (Path file : javaFiles(SOURCE_ROOT.resolve("ui"))) {
            String code = read(file);
            assertTrue(file + " importa android.database", !code.contains("import android.database"));
            assertTrue(file + " usa DatabaseHelper", !code.contains("DatabaseHelper"));
        }
    }

    @Test
    public void validationIsPureJava() throws IOException {
        for (Path file : javaFiles(SOURCE_ROOT.resolve("validation"))) {
            assertTrue(file + " importa android.*", !read(file).contains("import android."));
        }
    }

    @Test
    public void javaFilesHaveAtMost350Lines() throws IOException {
        for (Path file : javaFiles(SOURCE_ROOT)) {
            int lines = Files.readAllLines(file, StandardCharsets.UTF_8).size();
            assertTrue(file + " tem " + lines + " linhas", lines <= MAX_LINES);
        }
    }
}
