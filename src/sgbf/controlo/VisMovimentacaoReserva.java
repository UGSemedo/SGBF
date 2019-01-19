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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sgbf.modelo.ModAcervo;
import sgbf.modelo.ModReserva;
import sgbf.modelo.ModVisitante;
import sgbf.util.UtilControloExcessao;
import sgbf.util.UtilUsuarioLogado;
import sgbf.util.UtilValidarDados;

/**
 * FXML Controller class
 *
 * @author Marron
 */
public class VisMovimentacaoReserva implements Initializable {

    @FXML
    private JFXButton botaoPesquisar;
    @FXML
    private RadioButton radioButtonUtente, radioButtonAcervos;
    @FXML
    private ToggleGroup OpcoesDePesquisa;
    @FXML
    private TextField textFieldPesquisar, textFieldQuantidadeTotal,
            textFieldQuantidadeRemanescente, textFieldQuantidadeReservar, textFieldUtente;
    @FXML
    private Label labelOperador;
    @FXML
    private Button botaoReserva, botaoDevolver, botaoTodasReservas, botaoCancelar, botaoSair;
    @FXML
    private TableView<ModVisitante> tableVieVisitante;
    @FXML
    private TableColumn<ModVisitante, String> tableColumId, tableColumNome, tableColumIdTipoIdentificacao,
            tableColumNmeroIdentificacao, tableColumContacto, tableColumEndereco, tableColumCategoria;
    @FXML
    private TableView<ModAcervo> tableViewAcervo;
    @FXML
    private TableColumn<ModAcervo, String> tableColumTitulo,tableColumSubTitulo,tableColumEdicao,tableColumISBN,
            tableColumnCodigoBarra, tableColumnTipo, tableColumnFormato;
    @FXML
    private TableView<ModReserva> tableViewReserva;
    @FXML
    private AnchorPane anchoPaneReserva;

    private String operacao = null;
    private final ModVisitante visitanteMod = new ModVisitante();
    private final ModAcervo acervoMod = new ModAcervo();
    private final ConAcervo acervoCon = new ConAcervo();
    private final ConUtente utenteCon = new ConUtente();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.bloquearItensDaJanela();
        this.tableVieVisitante.setPlaceholder(new Label("Utentes não listados"));
        this.tableViewAcervo.setPlaceholder(new Label("Acervo não listados"));
        this.tableViewReserva.setPlaceholder(new Label("Nenhuma reserva feita"));
        this.labelOperador.setText(UtilUsuarioLogado.getUsuarioLogado().getNome());
        tableViewAcervo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.exibirAsQuantidades(newValue));
    }

    @FXML
    private void pesquisar() {
        RadioButton itemPesquisar = (RadioButton) OpcoesDePesquisa.getSelectedToggle();
        if (itemPesquisar != null) {
            if (radioButtonUtente.getText().equalsIgnoreCase(itemPesquisar.getText())) {
                this.pesquisarUtente();
            } else {
                if (radioButtonAcervos.getText().equalsIgnoreCase(itemPesquisar.getText())) {
                    this.pesquisarAcervos();
                }
            }
        } else {
            throw new UtilControloExcessao("Pesquisar item", "Seleccione o item a pesquisar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void cancelar() {
        this.bloquearItensDaJanela();
        this.limparItensAcervos();
        this.tableVieVisitante.getItems().clear();
        this.tableViewReserva.getItems().clear();
    }

    @FXML
    private void sair(ActionEvent event) {
        anchoPaneReserva.setVisible(false);
    }

    private void bloquearItensDaJanela() {
        this.bloquearItensAcervos();
        this.botaoDevolver.setDisable(true);
        this.botaoTodasReservas.setDisable(true);
        this.botaoDevolver.setDisable(true);
    }

    private void pesquisarUtente() {
        operacao = "Pesquisar Utente";
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        if (this.textFieldPesquisar.getText().isEmpty()) {
            throw new UtilControloExcessao(operacao, "Introduza o código ou nome do Utente", Alert.AlertType.INFORMATION);
        } else {
            todosRegistosEncontrados = this.utenteCon.pesquisar(this.pegarDadosDaPesquisaUtente(), operacao);
            if (todosRegistosEncontrados.isEmpty()) {
                this.tableVieVisitante.getItems().clear();
                throw new UtilControloExcessao(operacao, "Utente não encontradao", Alert.AlertType.INFORMATION);
            } else {
                this.carregarResultadosNaTableaUtente(todosRegistosEncontrados);
            }
        }
    }
    

    private ModVisitante pegarDadosDaPesquisaUtente() {
        if (UtilValidarDados.eNumero(this.textFieldPesquisar.getText())) {
            visitanteMod.setIdUtente(Integer.valueOf(this.textFieldPesquisar.getText()), operacao);
            visitanteMod.setPrimeiro_nome(this.textFieldPesquisar.getText(), operacao);
            return visitanteMod;
        } else {
            visitanteMod.setPrimeiro_nome(this.textFieldPesquisar.getText(), operacao);
            return visitanteMod;
        }
    }

    private void carregarResultadosNaTableaUtente(List<Object> todosRegistosEncontrados) {
        tableColumId.setCellValueFactory(new PropertyValueFactory<>("idUtente"));
        tableColumNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumIdTipoIdentificacao.setCellValueFactory(new PropertyValueFactory<>("tipo_identificacao"));
        tableColumNmeroIdentificacao.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        tableColumEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tableColumCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        tableVieVisitante.setItems(this.todosUtentesParaCarregar(todosRegistosEncontrados));
    }

    private ObservableList<ModVisitante> todosUtentesParaCarregar(List<Object> todosRegistosEncontrados) {
        List<ModVisitante> listaDosRegistosEncontrados = new ArrayList<>();
        for (Object utenteRegistado : todosRegistosEncontrados) {
            ModVisitante visitanteMod = (ModVisitante) utenteRegistado;
            listaDosRegistosEncontrados.add(visitanteMod);
        }
        return FXCollections.observableArrayList(listaDosRegistosEncontrados);
    }

    private void pesquisarAcervos() {
        operacao = "Pesquisar Acervos";
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        if (this.textFieldPesquisar.getText().isEmpty()) {
            throw new UtilControloExcessao(operacao, "Introduza o código ou nome do Acervo", Alert.AlertType.INFORMATION);
        } else {
            todosRegistosEncontrados = this.acervoCon.pesquisar(this.pegarDadosDaPesquisaAcervos(), operacao);
            if (todosRegistosEncontrados.isEmpty()) {
                this.bloquearItensAcervos();
                this.limparItensAcervos();
                throw new UtilControloExcessao(operacao, "Acervo não encontradao", Alert.AlertType.INFORMATION);
            } else {
                this.carregarResultadosNaTableAcervos(todosRegistosEncontrados);
            }
        }
    }

    private ModAcervo pegarDadosDaPesquisaAcervos() {
        if (UtilValidarDados.eNumero(this.textFieldPesquisar.getText())) {
            acervoMod.setIdAcervo(Integer.valueOf(this.textFieldPesquisar.getText()), operacao);
            acervoMod.setTitulo(this.textFieldPesquisar.getText(), operacao);
            return acervoMod;
        } else {
            acervoMod.setTitulo(this.textFieldPesquisar.getText(), operacao);
            return acervoMod;
        }
    }

    private void carregarResultadosNaTableAcervos(List<Object> todosRegistosEncontrados) {
        tableColumTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tableColumSubTitulo.setCellValueFactory(new PropertyValueFactory<>("sub_titulo"));
        tableColumISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tableColumnCodigoBarra.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo_acervo"));
        tableColumnFormato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        tableViewAcervo.setItems(this.todosAcervosParaCarregar(todosRegistosEncontrados));
    }

    private ObservableList<ModAcervo> todosAcervosParaCarregar(List<Object> todosRegistosEncontrados) {
        List<ModAcervo> listaDosRegistosWncontrados = new ArrayList<>();
        for (Object acervoRegistado : todosRegistosEncontrados) {
            ModAcervo acervoMod = (ModAcervo) acervoRegistado;
            listaDosRegistosWncontrados.add(acervoMod);
        }
        return FXCollections.observableArrayList(listaDosRegistosWncontrados);
    }
    
    private void exibirAsQuantidades(ModAcervo acervoMod){
        this.textFieldQuantidadeTotal.setText(String.valueOf(acervoMod.getEstoqueMod().getQuantidade_total()));
        this.textFieldQuantidadeRemanescente.setText(String.valueOf(acervoMod.getEstoqueMod().getQuantidadeRemanescente()));
        this.desbloquearItensAcervos();
    }
    
    private void limparItensAcervos(){
        this.textFieldQuantidadeTotal.setText(null);
        this.textFieldQuantidadeRemanescente.setText(null);
        this.textFieldQuantidadeReservar.setText(null);
        this.tableViewAcervo.getItems().clear();
    }
    
    private void bloquearItensAcervos(){
        this.textFieldQuantidadeReservar.setDisable(true);
        this.botaoReserva.setDisable(true);
    }
    
    private void desbloquearItensAcervos(){
        this.textFieldQuantidadeReservar.setDisable(false);
        this.botaoReserva.setDisable(false);
    }
    

}
