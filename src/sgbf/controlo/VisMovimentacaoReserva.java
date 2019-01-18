/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgbf.controlo;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sgbf.modelo.ModAcervo;
import sgbf.util.UtilControloExcessao;
import sgbf.util.UtilValidarDados;

/**
 * FXML Controller class
 *
 * @author Marron
 */
public class VisMovimentacaoReserva implements Initializable {    
    @FXML
    private TextField TextPesquisar;
    @FXML
    private TableView<ModAcervo> tableAcervo;
    @FXML
    private TableView<String> tableReserva;
    @FXML
    private JFXButton botaoPesquisar;
    @FXML
    private TextField idReserva;
    @FXML
    private TextField textNomeUtente;
    @FXML
    private TextField TextDiasReman;
    @FXML
    private ComboBox<String> comboReserva;
    @FXML
    private TextField idAcervo;
    @FXML
    private TextField TextQuant;
    @FXML
    private TextField textTitulo;
    @FXML
    private Button botaoNovo;
    @FXML
    private Button botaoReserva;
    @FXML
    private Button botaoDevolver;
    @FXML
    private Button botaoCancelar;
    @FXML
    private Button botaoSair;
    @FXML
    private TableColumn<ModAcervo, String> titulo;
    @FXML
    private TableColumn<ModAcervo, String> tipo;
    @FXML
    private TableColumn<ModAcervo, Integer> isbn;
    @FXML
    private TableColumn<ModAcervo, String> ano;
    @FXML
    private AnchorPane anchoPaneReserva;
    
    
    private String operacao = null;
    private final ModAcervo acervoMod = new ModAcervo();
    private final ConAcervo acervoCon = new ConAcervo();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       this.bloquearItensDaJanela();
       this.tableAcervo.setPlaceholder(new Label("Acervo não listados"));
       tableAcervo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.exibirDadosNosCampos(newValue));
    } 
    @FXML
    private void cancelar(){
        this.bloquearItensDaJanela();
        this.limparItensDaJanela();
    }
    @FXML
    private void sair(ActionEvent event) {
        anchoPaneReserva.setVisible(false);
    }
    
   @FXML
    private void desbloquearItensDaJanela(){
        this.TextQuant.setDisable(false);
        this.TextDiasReman.setDisable(false);
        this.textNomeUtente.setDisable(false);
        this.botaoNovo.setDisable(true);
        this.botaoReserva.setDisable(false);
    }
    private void bloquearItensDaJanela(){
        this.idReserva.setDisable(true);
        this.idAcervo.setDisable(true);
        this.textNomeUtente.setDisable(true);
        this.TextQuant.setDisable(true);
        this.TextDiasReman.setDisable(true);
        this.comboReserva.setDisable(true);
        this.botaoNovo.setDisable(false);
        this.botaoReserva.setDisable(true);
        this.botaoDevolver.setDisable(true);   
    }
    private void limparItensDaJanela(){
        this.idAcervo.setText(null);
        this.idReserva.setText(null);
        this.textNomeUtente.setText(null);
        this.TextQuant.setText(null);
        this.tableAcervo.getItems().clear();
    }
    private void botaoPesquisarAcervo(){
      
        operacao = "Pesquisar Acervo";
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        if(this.TextPesquisar.getText().isEmpty()){
           throw new UtilControloExcessao(operacao, "Introduza o código ou titulo do Acervo", Alert.AlertType.INFORMATION);
        }else{
            todosRegistosEncontrados = this.acervoCon.pesquisar(this.pegarDadosDaPesquisa(), operacao);
            if(todosRegistosEncontrados.isEmpty()){
                this.bloquearItensDaJanela();
                this.limparItensDaJanela();
               throw new UtilControloExcessao(operacao, "Acervo não encontradao", Alert.AlertType.INFORMATION);
            }else{
                this.carregarResultadosNaTablea(todosRegistosEncontrados);
                this.bloquearItensDaJanela();
            }
        }
    }
    private void exibirDadosNosCampos(ModAcervo acervoMod){
        if(tableAcervo.getSelectionModel().getSelectedCells().size() == 1){
            textTitulo.setText(acervoMod.getTitulo());
            botaoDevolver.setDisable(false);
            botaoReserva.setDisable(false);
            botaoNovo.setDisable(true);
            this.desbloquearItensDaJanela();
        }else{
            botaoDevolver.setDisable(true);
            botaoReserva.setDisable(true);
            botaoNovo.setDisable(false);
            this.limparItensDaJanela();
        }
    }
    private ModAcervo pegarDadosDaPesquisa(){
        if(UtilValidarDados.eNumero(this.TextPesquisar.getText())){
           acervoMod.setIdAcervo(Integer.valueOf(this.TextPesquisar.getText()), operacao);
           acervoMod.setTitulo(this.TextPesquisar.getText(), operacao);
           return acervoMod;
        }else{
           acervoMod.setTitulo(this.TextPesquisar.getText(), operacao);
           return acervoMod;
        }
    }
    private void carregarResultadosNaTablea(List<Object> todosRegistosEncontrados){
        titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo_acervo"));
        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        ano.setCellValueFactory(new PropertyValueFactory<>("ano_lancamento"));
        tableAcervo.setItems(this.todosRegistosParaCarregar(todosRegistosEncontrados));
    }
    private ObservableList<ModAcervo> todosRegistosParaCarregar(List<Object> todosRegistosEncontrados){
        List<ModAcervo> listaDosRegistosEncontrados = new ArrayList<>();
        for(Object acervoRegistado: todosRegistosEncontrados){
            ModAcervo acervoMod = (ModAcervo)acervoRegistado;
            listaDosRegistosEncontrados.add(acervoMod);
        } 
        return FXCollections.observableArrayList(listaDosRegistosEncontrados);
    }
}
