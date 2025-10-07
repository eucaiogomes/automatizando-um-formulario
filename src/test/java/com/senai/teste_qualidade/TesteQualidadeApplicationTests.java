// Teste Selenium simples e comentado para demonstração
package com.senai.teste_qualidade;

import io.github.bonigarcia.wdm.WebDriverManager; // gerencia chromedriver automaticamente
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths; // para construir file:/// corretamente
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste mínimo com Selenium para abrir o arquivo local index.html,
 * preencher email/senha, submeter e verificar a mensagem de feedback.
 *

 */
public class TesteQualidadeApplicationTests {

    // driver que controla o navegador
    private WebDriver driver;

    // espera explícita para sincronizar ações (evitar timing issues)
    private WebDriverWait wait;

    @BeforeEach
    public void setup() {
        // 1) Garante que o chromedriver correto esteja disponível (baixa automaticamente se necessário)
        WebDriverManager.chromedriver().setup();

        // 2) Cria a instância do ChromeDriver (abre o navegador)
        driver = new ChromeDriver();

        // 3) Cria um objeto de espera com timeout de 10 segundos
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Não maximizamos para evitar problemas em ambientes CI; se quiser, use driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        // Fecha o navegador e libera recursos
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testeVisualLoginSimples() throws InterruptedException {
        // Construímos uma URL file:/// que aponta para o index.html do projeto
        // Isso é preferível a usar o caminho Windows cru com backslashes
        String localPath = Paths.get("src/main/java/com/senai/teste_qualidade/index.html")
                .toAbsolutePath() // converte para caminho absoluto
                .toUri()          // transforma em URI (ex.: file:///C:/...)
                .toString();

        // Abre a página local no navegador
        driver.get(localPath);

        // -------------- Preenche o formulário --------------
        // Espera até que o campo de email esteja visível na página
        WebElement campoEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputEmail")));

        // Apaga qualquer texto pré-existente (boa prática)
        campoEmail.clear();

        // Digita um email válido de exemplo (este valor será tratado pelo script JS do index.html)
        campoEmail.sendKeys("Lucas@Lucas");

        // Pequena pausa só para você visualizar (opcional)
        Thread.sleep(1000);

        // Localiza o campo de senha (aqui não usamos wait porque já sabemos que o formulário carregou)
        WebElement campoSenha = driver.findElement(By.id("inputPassword"));
        campoSenha.clear();
        campoSenha.sendKeys("131415");

        // Outra pausa curta para ver o preenchimento
        Thread.sleep(1000);

        // Localiza o botão de submit. O seletor aceita <button> ou <input>
        WebElement botaoSubmit = driver.findElement(By.cssSelector("button[type='submit'], input[type='submit']"));

        // Clica no botão para submeter o formulário
        botaoSubmit.click();

        // -------------- Verificação do resultado --------------
        // Espera que o elemento com id="texto" fique visível (o index.html tem um script que escreve nele)
        WebElement mensagem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("texto")));

        // Pausa extra para você ver a mensagem antes da verificação automática
        Thread.sleep(2000);

        // Verifica que a mensagem contém o texto de sucesso esperado
        String texto = mensagem.getText();
        assertTrue(texto.contains("Login realizado com sucesso"), "Esperado mensagem de sucesso no login");
    }
}

