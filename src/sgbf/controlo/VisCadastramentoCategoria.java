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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sgbf.modelo.ModCategoria;
import sgbf.modelo.ModCategoriaDaEstante;
import sgbf.modelo.ModEstante;
import sgbf.util.UtilControloExcessao;
import sgbf.util.UtilValidarDados;

/**
 * FXML Controller class
 *
 * @author Marron
 */
public class VisCadastramentoCategoria implements Initializable {

    @FXML
    private JFXButton botaoPesquisar;
    @FXML
    private Button botaoCadastrar, botaoAlterar, botaoRemover, botaoNovo, botaoCancelar, botaoSair;
    @FXML
    private TextField texteFiedPesquisar, texteFiedDesigancao;
    @FXML
    private ComboBox<ModEstante> comboBoxEstante;
    @FXML
    private TableView<ModCategoria> tableViewCategoria;
    @FXML
    private TableColumn<ModCategoria, String> tableColumDesignacao;
    @FXML
    private TableColumn<ModCategoria, Integer> tableColumID;
    @FXML
    private AnchorPane AnchorPaneCategoria;

    private String operacao = null;
    private final ModCategoria categoriaMod = new ModCategoria();
    private final ConCategoria categoriaCon = new ConCategoria();
    private final ModCategoriaDaEstante categoriaDaEstanteMod = new ModCategoriaDaEstante();
    private final ConCategoriaDaEstante categoriaDaEstanteCon = new ConCategoriaDaEstante();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.bloquearItensDaJanela();
        this.tableViewCategoria.setPlaceholder(new Label("Categorias não listadas"));
        this.texteFiedPesquisar.setTooltip(new Tooltip("Introduza o código, desginação ou use *( _ ) para listar todos registos "));
        tableViewCategoria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.exibirDadosNosCampos(newValue));
    }

    @FXML
    private void cadastrarCategoria() {
        operacao = "Registar Categoria";
        categoriaMod.setDesignacao(texteFiedDesigancao.getText(), operacao);
        categoriaMod.setEstanteMod(comboBoxEstante.getSelectionModel().getSelectedItem(), operacao);
        if (categoriaCon.registar(categoriaMod, operacao)) {
            categoriaMod.setIdCategoria(categoriaCon.proximoCodigoASerRegistado(operacao), operacao);
            categoriaDaEstanteMod.setCategoriaMod(categoriaMod, operacao);
            categoriaDaEstanteMod.setEstanteMod(categoriaMod.getEstanteMod(), operacao);
            //if (categoriaDaEstanteCon.registar(categoriaDaEstanteMod, operacao)) {
            this.bloquearItensDaJanela();
            this.limparItensDaJanela();
            throw new UtilControloExcessao(operacao, "Categoria Cadastrada com sucesso", Alert.AlertType.CONFIRMATION);
            // }
        }
    }

    @FXML
    private void alterarCategoria() {
        operacao = "Editar Categoria";
        categoriaMod.setIdCategoria(this.tableViewCategoria.getSelectionModel().getSelectedItem().getIdCategoria(), operacao);
        categoriaMod.setDesignacao(texteFiedDesigancao.getText(), operacao);
        categoriaMod.setEstanteMod(comboBoxEstante.getSelectionModel().getSelectedItem(), operacao);
        categoriaMod.setEstanteModAntiga(this.tableViewCategoria.getSelectionModel().getSelectedItem(), operacao);
        categoriaDaEstanteMod.setCategoriaMod(categoriaMod, operacao);
        categoriaDaEstanteMod.setEstanteMod(categoriaMod.getEstanteMod(), operacao);
        if (categoriaCon.alterar(categoriaMod, operacao)) {
            this.bloquearItensDaJanela();
            this.limparItensDaJanela();
            throw new UtilControloExcessao(operacao, "Categoria editada com sucesso", Alert.AlertType.CONFIRMATION);
        }
    }

    @FXML
    private void removerCategoria() {
        operacao = "Remover Categoria";
        ModCategoria categoriaARemover = this.tableViewCategoria.getSelectionModel().getSelectedItem();
        if (categoriaCon.remover(categoriaARemover, operacao)) {
            this.tableViewCategoria.getItems().remove(categoriaARemover);
            this.bloquearItensDaJanela();
            throw new UtilControloExcessao(operacao, "Categoria removida com sucesso", Alert.AlertType.CONFIRMATION);
        }
    }

    @FXML
    private void pesquisarCategoria() {
        operacao = "Pesquisar Categoria";
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        if (this.texteFiedPesquisar.getText().isEmpty()) {
            throw new UtilControloExcessao(operacao, "Introduza o código ou designação da categoria", Alert.AlertType.INFORMATION);
        } else {
            todosRegistosEncontrados = this.categoriaCon.pesquisar(this.pegarDadosDaPesquisa(), operacao);
            if (todosRegistosEncontrados.isEmpty()) {
                this.bloquearItensDaJanela();
                this.limparItensDaJanela();
                throw new UtilControloExcessao(operacao, "Categoria não encontrada", Alert.AlertType.INFORMATION);
            } else {
                this.carregarResultadosNaTablea(todosRegistosEncontrados);
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
    }

    @FXML
    private void sair(ActionEvent event) {
        AnchorPaneCategoria.setVisible(false);
    }

    @FXML
    private void desbloquearItensDaJanela() {
        this.texteFiedDesigancao.setDisable(false);
        this.comboBoxEstante.setDisable(false);
        this.botaoNovo.setDisable(true);
        this.botaoCadastrar.setDisable(false);
    }

    private void bloquearItensDaJanela() {
        this.texteFiedDesigancao.setDisable(true);
        this.comboBoxEstante.setDisable(true);
        this.botaoNovo.setDisable(false);
        this.botaoCadastrar.setDisable(true);
        this.botaoAlterar.setDisable(true);
        this.botaoRemover.setDisable(true);
    }

    private void limparItensDaJanela() {
        this.texteFiedPesquisar.setText(null);
        this.texteFiedDesigancao.setText(null);
        this.tableViewCategoria.getItems().clear();
    }

    private void carregarValorNasComboxs() {
        this.comboBoxEstante.getItems().clear();
        ConEstante estanteCon = new ConEstante();
        List<ModEstante> todasEstantes = new ArrayList<>();
        ObservableList todasEstantesParaCombox = null;

        todasEstantes.add(new ModEstante());
        for (Object todosRegistos : estanteCon.listarTodos("Cadastramento de Categoria")) {
            ModEstante estanteRegistada = (ModEstante) todosRegistos;
            todasEstantes.add(estanteRegistada);
        }
        todasEstantesParaCombox = FXCollections.observableArrayList(todasEstantes);
        this.comboBoxEstante.setItems(todasEstantesParaCombox);

    }

    private void exibirDadosNosCampos(ModCategoria categoriaMod) {
        if (tableViewCategoria.getSelectionModel().getSelectedCells().size() == 1) {
            this.carregarValorNasComboxs();
            texteFiedDesigancao.setText(String.valueOf(categoriaMod.getDesignacao()));
            for (int i = 0; i < comboBoxEstante.getItems().size(); i++) {
                if (categoriaMod.getEstanteMod() != null) {
                    comboBoxEstante.getSelectionModel().select(i);
                    if (categoriaMod.getEstanteMod().getIdEstante() == comboBoxEstante.getSelectionModel().getSelectedItem().getIdEstante()) {
                        break;
                    }
                }
            }
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

    private ModCategoria pegarDadosDaPesquisa() {
        if (UtilValidarDados.eNumero(this.texteFiedPesquisar.getText())) {
            categoriaMod.setIdCategoria(Integer.valueOf(this.texteFiedPesquisar.getText()), operacao);
            categoriaMod.setDesignacao(this.texteFiedPesquisar.getText(), operacao);
            return categoriaMod;
        } else {
            categoriaMod.setDesignacao(this.texteFiedPesquisar.getText(), operacao);
            return categoriaMod;
        }
    }

    private void carregarResultadosNaTablea(List<Object> todosRegistosEncontrados) {
        tableColumID.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        tableColumDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        tableViewCategoria.setItems(this.todosRegistosParaCarregar(todosRegistosEncontrados));
    }

    private ObservableList<ModCategoria> todosRegistosParaCarregar(List<Object> todosRegistosEncontrados) {
        List<ModCategoria> listaDosRegistosWncontrados = new ArrayList<>();
        for (Object categoriaRegistado : todosRegistosEncontrados) {
            ModCategoria categoriaMod = (ModCategoria) categoriaRegistado;
            listaDosRegistosWncontrados.add(categoriaMod);
        }
        return FXCollections.observableArrayList(listaDosRegistosWncontrados);
    }

}
