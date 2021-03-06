/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgbf.controlo;

import sgbf.dao.DaoFuncionario;
import sgbf.dao.DaoUtente;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sgbf.modelo.ModFuncionario;
import sgbf.modelo.ModVisitante;
import sgbf.util.UtilControloExcessao;
import sgbf.util.UtilUsuarioLogado;
import sgbf.util.UtilValidarDados;

/**
 * FXML Controller class
 *
 * @author Marron
 */
public class VisCadastramentoFuncionario implements Initializable {

    @FXML
    private Button botaoCadastrar, botaoAlterar, botaoRemover, botaoNovo, botaoCancelar, botaoSair;
    @FXML
    private TextField texteFiedPesquisarUtente, texteFiedPesquisarFuncionario,
            texteFiedcodigoFuncionario, texteFiedcodigoUtente;
    @FXML
    private JFXButton botaoPesquisarUtente, botaoPesquisarFuncionario;
    @FXML
    private ComboBox<String> comboBoxCargo;
    @FXML
    private TableView<ModVisitante> tableViewVisitane;
    @FXML
    private TableView<ModFuncionario> tableViewFuncionario;
    @FXML
    private TableColumn<ModVisitante, String> tableColumNomeUtente, tableColumIdTipoIdentificacaoUtente,
            tableColumNmeroIdentificacaoUtente, tableColumContactoUtente;
    @FXML
    private TableColumn<ModFuncionario, String> tableColumNomeFuncionario,
            tableColumCategoriaFuncionario, tableColumContactoFuncionario;
    private String operacao = null;
    private final ModVisitante utenteMod = new ModVisitante();
    private final ModFuncionario funcionarioMod = new ModFuncionario();
    private final DaoUtente utenteCon = new DaoUtente();
    private final DaoFuncionario funcionarioCon = new DaoFuncionario();
    @FXML
    private AnchorPane AnchorPaneFuncionario;
    @FXML
    private Label labelOperador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.bloquearItensDaJanela();
        this.exibirMensagemNasTabelas();
        this.texteFiedPesquisarUtente.setTooltip(new Tooltip("Introduza o código, nome do utente ou use *( _ ) para listar todos registos "));
        this.texteFiedPesquisarFuncionario.setTooltip(new Tooltip("Introduza o código, nome do funcionário ou use *( _ ) para listar todos registos "));
        this.tableViewVisitane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> this.exibirDadosDoUtenteNosCampos(newValue));
        this.tableViewFuncionario.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> this.exibirDadosDoFuncionarioNosCampos(newValue));
    }

    @FXML
    private void cadastrarFuncionario() {
        operacao = "Registar Funcionário";
        if (tableViewVisitane.getSelectionModel().getSelectedCells().size() == 1) {
            funcionarioMod.setCodigoFuncionario(texteFiedcodigoFuncionario.getText(), operacao);
            funcionarioMod.setCargo(comboBoxCargo.getSelectionModel().getSelectedItem(), operacao);
            funcionarioMod.setIdUtente(tableViewVisitane.getSelectionModel().getSelectedItem().getIdUtente(), operacao);
            if (funcionarioCon.registar(funcionarioMod, operacao)) {
                this.bloquearItensDaJanela();
                this.limparItensDaJanela();
                throw new UtilControloExcessao(operacao, "Funcionário Cadastrado com sucesso", Alert.AlertType.CONFIRMATION);
            }
        } else {
            throw new UtilControloExcessao(operacao, "Seleccione o Utente a Cadastrar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void alterarFuncionario() {
        operacao = "Alterar Funcionário";
        if (tableViewFuncionario.getSelectionModel().getSelectedCells().size() == 1) {
            funcionarioMod.setCodigoFuncionario(texteFiedcodigoFuncionario.getText(), operacao);
            funcionarioMod.setCargo(comboBoxCargo.getSelectionModel().getSelectedItem(), operacao);
            funcionarioMod.setIdUtente(tableViewVisitane.getSelectionModel().getSelectedItem().getIdUtente(), operacao);
            if (funcionarioCon.alterar(funcionarioMod, operacao)) {
                this.bloquearItensDaJanela();
                this.limparItensDaJanela();
                throw new UtilControloExcessao(operacao, "Funcionário editado com sucesso", Alert.AlertType.CONFIRMATION);
            }
        } else {
            throw new UtilControloExcessao(operacao, "Seleccione o Funcionário a alterar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void removerUtente() {
        operacao = "Remover Funcionário";
        if (tableViewFuncionario.getSelectionModel().getSelectedCells().size() == 1) {
            ModFuncionario funcionarioARemover = this.tableViewFuncionario.getSelectionModel().getSelectedItem();
            if (funcionarioCon.remover(funcionarioARemover, operacao)) {
                this.tableViewFuncionario.getItems().remove(funcionarioARemover);
                this.bloquearItensDaJanela();
                throw new UtilControloExcessao(operacao, "Funcionário removido com sucesso", Alert.AlertType.CONFIRMATION);
            }
        } else {
            throw new UtilControloExcessao(operacao, "Seleccione o Funcionário a remover", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void pesquisarUtente() {
        operacao = "Pesquisar Utente";
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        if (this.texteFiedPesquisarUtente.getText().isEmpty()) {
            throw new UtilControloExcessao(operacao, "Introduza o código ou nome do Utente", Alert.AlertType.INFORMATION);
        } else {
            todosRegistosEncontrados = this.utenteCon.pesquisar(this.pegarDadosDaPesquisaUtente(), operacao);
            if (todosRegistosEncontrados.isEmpty()) {
                this.bloquearItensDaJanela();
                this.limparItensDaJanela();
                throw new UtilControloExcessao(operacao, "Utente não encontradao", Alert.AlertType.INFORMATION);
            } else {
                this.carregarResultadosNaTableaUtente(todosRegistosEncontrados);
                this.bloquearItensDaJanela();
            }
        }
    }

    @FXML
    private void pesquisarFuncionario() {
        operacao = "Pesquisar Funcionário";
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        if (this.texteFiedPesquisarFuncionario.getText().isEmpty()) {
            throw new UtilControloExcessao(operacao, "Introduza o código ou nome do Funcionário", Alert.AlertType.INFORMATION);
        } else {
            todosRegistosEncontrados = this.funcionarioCon.pesquisar(this.pegarDadosDaPesquisaFuncionario(), operacao);
            if (todosRegistosEncontrados.isEmpty()) {
                this.bloquearItensDaJanela();
                this.limparItensDaJanela();
                throw new UtilControloExcessao(operacao, "Funcionário não encontradao", Alert.AlertType.INFORMATION);
            } else {
                this.carregarResultadosNaTableaUtenteFuncionario(todosRegistosEncontrados);
                this.bloquearItensDaJanela();
            }
        }
    }

    @FXML
    private void novo() {
        this.desbloquearItensDaJanela();
        this.limparItensDaJanela();
        this.carregarValorNasComboxs();
    }

    @FXML
    private void cancelar() {
        this.bloquearItensDaJanela();
        this.limparItensDaJanela();
        this.tableViewVisitane.getItems().clear();
    }

    @FXML
    private void sair(ActionEvent event) {
        AnchorPaneFuncionario.setVisible(false);
    }

    private void desbloquearItensDaJanela() {
        this.texteFiedcodigoFuncionario.setDisable(false);
        this.comboBoxCargo.setDisable(false);
        this.botaoNovo.setDisable(true);
        this.botaoCadastrar.setDisable(false);
    }

    private void bloquearItensDaJanela() {
        this.texteFiedcodigoFuncionario.setDisable(true);
        this.texteFiedcodigoUtente.setDisable(true);
        this.comboBoxCargo.setDisable(true);
        this.botaoNovo.setDisable(false);
        this.botaoCadastrar.setDisable(true);
        this.botaoAlterar.setDisable(true);
        this.botaoRemover.setDisable(true);
    }

    private void carregarValorNasComboxs() {
        this.comboBoxCargo.getItems().addAll("Administrador", "Bibliotecario");
    }

    private void exibirMensagemNasTabelas() {
        this.tableViewVisitane.setPlaceholder(new Label("Utentes não listados"));
        this.tableViewFuncionario.setPlaceholder(new Label("Funcionários não listados"));
    }

    private void exibirDadosDoUtenteNosCampos(ModVisitante visitanteMod) {
        if (tableViewVisitane.getSelectionModel().getSelectedCells().size() == 1) {
            this.carregarValorNasComboxs();
            texteFiedcodigoUtente.setText(String.valueOf(visitanteMod.getIdUtente()));
        } else {
            texteFiedcodigoUtente.setText(null);
        }
    }

    private void exibirDadosDoFuncionarioNosCampos(ModFuncionario funcionarioMod) {
        if (tableViewFuncionario.getSelectionModel().getSelectedCells().size() == 1) {
            this.carregarValorNasComboxs();
            texteFiedcodigoFuncionario.setText(funcionarioMod.getCodigoFuncionario());
            comboBoxCargo.getSelectionModel().select(funcionarioMod.getCargo());
            texteFiedcodigoUtente.setText(String.valueOf(funcionarioMod.getIdUtente()));
            this.funcionarioMod.setIdFuncionario(funcionarioMod.getIdFuncionario(), operacao);
            botaoAlterar.setDisable(false);
            botaoRemover.setDisable(false);
            this.desbloquearItensDaJanela();
            botaoNovo.setDisable(true);
            botaoCadastrar.setDisable(true);
        } else {
            botaoAlterar.setDisable(true);
            botaoRemover.setDisable(true);
            botaoNovo.setDisable(false);
            this.limparItensDaJanela();
        }
    }

    private void limparItensDaJanela() {
        this.texteFiedPesquisarUtente.setText(null);
        this.texteFiedPesquisarFuncionario.setText(null);
        this.texteFiedcodigoFuncionario.setText(null);
        this.texteFiedcodigoUtente.setText(null);
        this.tableViewFuncionario.getItems().clear();
        this.comboBoxCargo.getItems().clear();
    }

    private ModVisitante pegarDadosDaPesquisaUtente() {
        if (UtilValidarDados.eNumero(this.texteFiedPesquisarUtente.getText())) {
            utenteMod.setIdUtente(Integer.valueOf(this.texteFiedPesquisarUtente.getText()), operacao);
            utenteMod.setPrimeiro_nome(this.texteFiedPesquisarUtente.getText(), operacao);
            return utenteMod;
        } else {
            utenteMod.setPrimeiro_nome(this.texteFiedPesquisarUtente.getText(), operacao);
            return utenteMod;
        }
    }

    private ModFuncionario pegarDadosDaPesquisaFuncionario() {
        if (UtilValidarDados.eNumero(this.texteFiedPesquisarFuncionario.getText())) {
            funcionarioMod.setIdUtente(Integer.valueOf(this.texteFiedPesquisarFuncionario.getText()), operacao);
            funcionarioMod.setPrimeiro_nome(this.texteFiedPesquisarFuncionario.getText(), operacao);
            return funcionarioMod;
        } else {
            funcionarioMod.setPrimeiro_nome(this.texteFiedPesquisarFuncionario.getText(), operacao);
            return funcionarioMod;
        }
    }

    private void carregarResultadosNaTableaUtente(List<Object> todosRegistosEncontrados) {
        tableColumNomeUtente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumIdTipoIdentificacaoUtente.setCellValueFactory(new PropertyValueFactory<>("tipo_identificacao"));
        tableColumNmeroIdentificacaoUtente.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumContactoUtente.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        tableViewVisitane.setItems(this.todosUtentesParaCarregar(todosRegistosEncontrados));
    }

    private void carregarResultadosNaTableaUtenteFuncionario(List<Object> todosRegistosEncontrados) {
        tableColumNomeFuncionario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumCategoriaFuncionario.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        tableColumContactoFuncionario.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        tableViewFuncionario.setItems(this.todosFuncionariosParaCarregar(todosRegistosEncontrados));
    }

    private ObservableList<ModVisitante> todosUtentesParaCarregar(List<Object> todosRegistosEncontrados) {
        List<ModVisitante> listaDosRegistosWncontrados = new ArrayList<>();
        for (Object utenteRegistado : todosRegistosEncontrados) {
            ModVisitante visitanteMod = (ModVisitante) utenteRegistado;
            if (visitanteMod.getCategoria().equalsIgnoreCase("Administrador")) {
                listaDosRegistosWncontrados.add(visitanteMod);
            } else {
                if (visitanteMod.getCategoria().equalsIgnoreCase("Funcionário")) {
                    listaDosRegistosWncontrados.add(visitanteMod);
                }
            }
        }
        if (listaDosRegistosWncontrados.isEmpty()) {
            throw new UtilControloExcessao(operacao, "Utente não encontradao", Alert.AlertType.INFORMATION);
        } else {
            return FXCollections.observableArrayList(listaDosRegistosWncontrados);
        }
    }

    private ObservableList<ModFuncionario> todosFuncionariosParaCarregar(List<Object> todosRegistosEncontrados) {
        List<ModFuncionario> listaDosRegistosWncontrados = new ArrayList<>();
        for (Object utenteRegistado : todosRegistosEncontrados) {
            ModFuncionario funcionarioMod = (ModFuncionario) utenteRegistado;
            listaDosRegistosWncontrados.add(funcionarioMod);
        }
        return FXCollections.observableArrayList(listaDosRegistosWncontrados);
    }

}
