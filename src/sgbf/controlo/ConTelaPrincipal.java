package sgbf.controlo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sgbf.util.UtilControloExcessao;

/**
 * @author Marron
 */
public class ConTelaPrincipal implements Initializable {

    @FXML
    private MenuItem menuitemCadastroAcervo;
    @FXML
    private MenuItem menuitemCadastroArea;
    @FXML
    private MenuItem menuitemCadastroAutor;
    @FXML
    private MenuItem menuitemCadastroCategoria;
    @FXML
    private MenuItem menuitemCadastroEditora;
    @FXML
    private MenuItem menuitemCadastroEstante;
    @FXML
    private MenuItem menuitemCadastroEstoque;
    @FXML
    private MenuItem menuitemCadastroFuncionario;
    @FXML
    private MenuItem menuitemCadastroProveniencia;
    @FXML
    private MenuItem menuitemCadastroUtente;
    @FXML
    private MenuItem menuitemVerUtentes;
    @FXML
    private MenuItem menuitemVerProveniencias;
    @FXML
    private MenuItem menuitemVerFuncionarios;
    @FXML
    private MenuItem menuitemVerEstoques;
    @FXML
    private MenuItem menuitemVerEstantes;
    @FXML
    private MenuItem menuitemVerEditoras;
    @FXML
    private MenuItem menuitemVerCategorias;
    @FXML
    private MenuItem menuitemVerAutores;
    @FXML
    private MenuItem menuitemVerAreas;
    @FXML
    private MenuItem menuitenmVerAcervos;
    @FXML
    private MenuItem menuitemMovimentacaoEmprestimo;
    @FXML
    private MenuItem menuitemMovimentacaoReserva;
    @FXML
    private MenuItem menuitemMovimentacaoDevolucao;
    @FXML
    private MenuItem menuItemTerminarSessao;
    @FXML
    private MenuItem menuItemSairDoSistema;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuBar menuBar;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    public void BotaoMenuItemUtente() throws IOException{
        AnchorPane x = (AnchorPane) FXMLLoader.load(getClass().getResource("/sgbf/visao/VisUtente.fxml"));
        anchorPane.getChildren().setAll(x);         
    }
    @FXML
    public void BotaoMenuItemFuncionario() throws IOException{
        AnchorPane x = (AnchorPane) FXMLLoader.load(getClass().getResource("/sgbf/visao/VisCadastroFuncionario.fxml"));
        anchorPane.getChildren().setAll(x);         
    }
    
    @FXML
    public void BotaoMenuItemEstante() throws IOException{
        AnchorPane x = (AnchorPane) FXMLLoader.load(getClass().getResource("/sgbf/visao/VisCadastroEstante.fxml"));
        anchorPane.getChildren().setAll(x);         
    }
    
    @FXML
    public void BotaoMenuItemEditora() throws IOException{
        AnchorPane x = (AnchorPane) FXMLLoader.load(getClass().getResource("/sgbf/visao/VisCadastroEditora.fxml"));
        anchorPane.getChildren().setAll(x);         
    }
    
    @FXML
    public void BotaoMenuItemCategoria() throws IOException{
        AnchorPane x = (AnchorPane) FXMLLoader.load(getClass().getResource("/sgbf/visao/VisCadastroCategoria.fxml"));
        anchorPane.getChildren().setAll(x);         
    }
    
    @FXML
    public void BotaoMenuItemArea() throws IOException{
        AnchorPane x = (AnchorPane) FXMLLoader.load(getClass().getResource("/sgbf/visao/VisCadastroArea.fxml"));
        anchorPane.getChildren().setAll(x);         
    }
    
    @FXML
    public void BotaoMenuItemAcervo() throws IOException{
        AnchorPane x = (AnchorPane) FXMLLoader.load(getClass().getResource("/sgbf/visao/VisCadastroAcervo.fxml"));
        anchorPane.getChildren().setAll(x);         
    }
    
    @FXML
    public void botaoMenuItemTerminarSessao()  throws IOException{
        UtilControloExcessao confirmar = new UtilControloExcessao();
        final String mensagem = "Tem a certeza que pretende terminar sessão ?";
        if(confirmar.temCerteza(menuItemTerminarSessao.getText(), mensagem)){
            Stage propriedadeDaJanela = (Stage) menuBar.getScene().getWindow();
            propriedadeDaJanela.close();

            Parent root = FXMLLoader.load(this.getClass().getResource("..\\visao\\VisLogin.fxml"));
            Scene scene = new Scene(root);
            propriedadeDaJanela.setScene(scene);
            propriedadeDaJanela.setTitle("Sistema de Gestão de Biblioteca");
            propriedadeDaJanela.setResizable(false);
            propriedadeDaJanela.setMaximized(false);
            propriedadeDaJanela.show();
        }
    }
    
    @FXML
    public void botaoMenuItemSairDoSistema(){
        Stage propriedadeDaJanela = (Stage) menuBar.getScene().getWindow();
        ConPrincipal.sairdoSistema(menuItemSairDoSistema.getText(), propriedadeDaJanela);
    }
    
}
