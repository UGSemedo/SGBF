package sgbf.controlo;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import sgbf.modelo.ModAcervo;
import sgbf.modelo.ModAutor;
import sgbf.modelo.ModEditora;
import sgbf.util.UtilControloExcessao;
import sgbf.util.UtilValidarDados;

/**
 * FXML Controller class
 *
 * @author Marron
 */
public class VisCadastramentoAcervo implements Initializable {
    
    @FXML
    private JFXButton botaoPesquisar;
    @FXML
    private Button botaoCadastrar, botaoAlterar, botaoRemover, botaoNovo, botaoCancelar, botaoSair;
    @FXML
    private TextField texteFiedPesquisar,texteFiedTitulo, texteFiedSubTitulo, texteFiedEdicao,
            texteFiedVolume, texteFiedNumPaginas, texteFieldAno, texteFiedCodigoBarra, texteFiedISBN,
            texteFiedEndereco;
    @FXML
    private ComboBox<String> comboBoxTipo, comboBoxFormato,comboBoxIdioma;
    @FXML
    private ComboBox<ModAutor> comboBoxAutor;
    @FXML
    private ComboBox<ModEditora> comboBoxEditora;
    @FXML
    private TableView<ModAcervo> tableViewAcervo; 
    @FXML
    private TableColumn<ModAcervo, String> tableColumTitulo,tableColumSubTitulo,tableColumEdicao,tableColumISBN,
            tableColumnAno, tableColumnTipo, tableColumnFormato;
    @FXML
    private JFXTextArea textAreaSinopese;
    @FXML
    private AnchorPane AnchorPaneAcervo;
    
    private String operacao = null;
    private final ModAcervo acervoMod = new ModAcervo();
    private final ConAcervo acervoCon = new ConAcervo();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       this.bloquearItensDaJanela();
       this.carregarValorNasComboxs();
       this.tableViewAcervo.setPlaceholder(new Label("Acervos não listados"));
       tableViewAcervo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.exibirDadosNosCampos(newValue));
    }    
    
    
    @FXML
    private void cadastrarAcervos(){
        operacao = "Registar Acervos";
        try{
            acervoMod.setTitulo(texteFiedTitulo.getText(), operacao);
            acervoMod.setSub_titulo(texteFiedSubTitulo.getText(), operacao);
            acervoMod.setTipo_acervo(comboBoxTipo.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setFormato(comboBoxFormato.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setEdicao(Byte.valueOf(texteFiedEdicao.getText()), operacao);
            acervoMod.setVolume(Byte.valueOf(texteFiedVolume.getText()), operacao);
            acervoMod.setNumero_paginas(Short.valueOf(texteFiedNumPaginas.getText()), operacao);
            acervoMod.setAno_lancamento( Integer.valueOf(texteFieldAno.getText()), operacao);
            acervoMod.setIdioma(comboBoxIdioma.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setEditoraMod(comboBoxEditora.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setCodigo_barra(texteFiedCodigoBarra.getText(), operacao);
            acervoMod.setIsbn(texteFiedISBN.getText(), operacao);
            acervoMod.setSinopse(textAreaSinopese.getText(), operacao);
            acervoMod.setAutorMod(comboBoxAutor.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setEndereco_acervo(texteFiedEndereco.getText(), operacao);
            if(acervoCon.registar(acervoMod, operacao)){
               this.bloquearItensDaJanela();
               this.limparItensDaJanela();
               throw new UtilControloExcessao(operacao, "Estante Cadastrada com sucesso", Alert.AlertType.CONFIRMATION);
            }
        }catch(NumberFormatException erro){
           throw new UtilControloExcessao(operacao, "Esta operção não poder executada\n"
                   + "Erro: A linha ou coluna excedeu o valor máximo (127) permitido", Alert.AlertType.WARNING);
        }
    }
    
    
    @FXML
    private void alterarAcervo(){
        operacao = "Editar Estante";
        try{
            acervoMod.setIdAcervo(this.tableViewAcervo.getSelectionModel().getSelectedItem().getIdAcervo(), operacao);
            acervoMod.setTitulo(texteFiedTitulo.getText(), operacao);
            acervoMod.setSub_titulo(texteFiedSubTitulo.getText(), operacao);
            acervoMod.setTipo_acervo(comboBoxTipo.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setFormato(comboBoxFormato.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setEdicao(Byte.valueOf(texteFiedEdicao.getText()), operacao);
            acervoMod.setVolume(Byte.valueOf(texteFiedVolume.getText()), operacao);
            acervoMod.setNumero_paginas(Short.valueOf(texteFiedNumPaginas.getText()), operacao);
            acervoMod.setAno_lancamento( Integer.valueOf(texteFieldAno.getText()), operacao);
            acervoMod.setIdioma(comboBoxIdioma.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setEditoraMod(comboBoxEditora.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setCodigo_barra(texteFiedCodigoBarra.getText(), operacao);
            acervoMod.setIsbn(texteFiedISBN.getText(), operacao);
            acervoMod.setSinopse(textAreaSinopese.getText(), operacao);
            acervoMod.setAutorMod(comboBoxAutor.getSelectionModel().getSelectedItem(), operacao);
            acervoMod.setEndereco_acervo(texteFiedEndereco.getText(), operacao);
            if(acervoCon.alterar(acervoMod, operacao)){
               this.bloquearItensDaJanela();
               this.limparItensDaJanela();
               throw new UtilControloExcessao(operacao, "Estante Cadastrada com sucesso", Alert.AlertType.CONFIRMATION);
            }
        }catch(NumberFormatException erro){
           throw new UtilControloExcessao(operacao, "Esta operção não poder executada\n"
                   + "Erro: A linha ou coluna excedeu o valor máximo (127) permitido", Alert.AlertType.WARNING);
        }
       
        
    }
    
    @FXML
    private void removerAcervo(){
        operacao = "Remover Estante";
        ModAcervo acervoARemover = this.tableViewAcervo.getSelectionModel().getSelectedItem();
        if(acervoCon.remover(acervoARemover, operacao)){
           this.tableViewAcervo.getItems().remove(acervoARemover);
           this.bloquearItensDaJanela();
           throw new UtilControloExcessao(operacao, "Estante removida com sucesso", Alert.AlertType.CONFIRMATION);
        }
    }
    
    @FXML
    private void pesquisarAcervo(){
        operacao = "Pesquisar Estante";
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        if(this.texteFiedPesquisar.getText().isEmpty()){
           throw new UtilControloExcessao(operacao, "Introduza o código ou designação da estante", Alert.AlertType.INFORMATION);
        }else{
            todosRegistosEncontrados = this.acervoCon.pesquisar(this.pegarDadosDaPesquisa(), operacao);
            if(todosRegistosEncontrados.isEmpty()){
                this.bloquearItensDaJanela();
                this.limparItensDaJanela();
               throw new UtilControloExcessao(operacao, "Estante não encontrada", Alert.AlertType.INFORMATION);
            }else{
                this.carregarResultadosNaTablea(todosRegistosEncontrados);
                this.bloquearItensDaJanela();
            }
        }
    }
    
    @FXML
    private void novo(){
        this.desbloquearItensDaJanela();
        this.limparItensDaJanela();
    }
    @FXML
    private void cancelar(){
        this.bloquearItensDaJanela();
        this.limparItensDaJanela();
    }
    
    @FXML
    private void sair(ActionEvent event) {
        AnchorPaneAcervo.setVisible(false);
    }
   
    @FXML
    private void desbloquearItensDaJanela(){
        this.texteFiedTitulo.setDisable(false);
        this.texteFiedSubTitulo.setDisable(false);
        this.comboBoxTipo.setDisable(false);
        this.comboBoxFormato.setDisable(false);
        this.texteFiedEdicao.setDisable(false);
        this.texteFiedVolume.setDisable(false);
        this.texteFiedNumPaginas.setDisable(false);
        this.texteFieldAno.setDisable(false);
        this.comboBoxIdioma.setDisable(false);
        this.comboBoxEditora.setDisable(false);
        this.texteFiedCodigoBarra.setDisable(false);
        this.texteFiedISBN.setDisable(false);
        this.textAreaSinopese.setDisable(false);
        this.comboBoxAutor.setDisable(false);
        this.botaoNovo.setDisable(true);
        this.botaoCadastrar.setDisable(false);
    }
    
    private void bloquearItensDaJanela(){
        this.texteFiedTitulo.setDisable(true);
        this.texteFiedSubTitulo.setDisable(true);
        this.comboBoxTipo.setDisable(true);
        this.comboBoxFormato.setDisable(true);
        this.texteFiedEdicao.setDisable(true);
        this.texteFiedVolume.setDisable(true);
        this.texteFiedNumPaginas.setDisable(true);
        this.texteFieldAno.setDisable(true);
        this.comboBoxIdioma.setDisable(true);
        this.comboBoxEditora.setDisable(true);
        this.texteFiedCodigoBarra.setDisable(true);
        this.texteFiedISBN.setDisable(true);
        this.textAreaSinopese.setDisable(true);
        this.comboBoxAutor.setDisable(true);
        this.botaoNovo.setDisable(false);
        this.botaoCadastrar.setDisable(true);
        this.botaoAlterar.setDisable(true);
        this.botaoRemover.setDisable(true);
    }
    
    private void limparItensDaJanela(){
        this.texteFiedTitulo.setText(null);
        this.texteFiedSubTitulo.setText(null);
        this.texteFiedEdicao.setText(null);
        this.texteFiedVolume.setText(null);
        this.texteFiedNumPaginas.setText(null);
        this.texteFieldAno.setText(null);
        this.texteFiedCodigoBarra.setText(null);
        this.texteFiedISBN.setText(null);
        this.textAreaSinopese.setText(null);
        this.texteFiedPesquisar.setText(null);
        this.tableViewAcervo.getItems().clear();
    }
   
    private void carregarValorNasComboxs(){
        ConEditora editoraCon = new ConEditora();
        ConAutor autorCon = new ConAutor();
        List<ModEditora> todasEditoras = new ArrayList<>();
        List<ModAutor> todosAutores = new ArrayList<>();
        ObservableList todasEditorasParaCombox =null;
        ObservableList todasAutoresParaCombox =null;
        
        for(Object todosRegistos: editoraCon.listarTodos("Cadastramento de Estante")){
            ModEditora editoraRegistada = (ModEditora)todosRegistos;
            todasEditoras.add(editoraRegistada);
        }
        todasEditorasParaCombox = FXCollections.observableArrayList(todasEditoras);
        this.comboBoxEditora.setItems(todasEditorasParaCombox);
      
        for(Object todosRegistos: autorCon.listarTodos("Cadastramento de Estante")){
            ModAutor autorRegistado = (ModAutor)todosRegistos;
            todosAutores.add(autorRegistado);
        }
        todasAutoresParaCombox = FXCollections.observableArrayList(todosAutores);
        this.comboBoxAutor.setItems(todasAutoresParaCombox);
      
        
    }

    
    private void exibirDadosNosCampos(ModAcervo acervoMod){
        if(tableViewAcervo.getSelectionModel().getSelectedCells().size() == 1){
            this.texteFiedTitulo.setText(acervoMod.getTipo_acervo());
            this.texteFiedSubTitulo.setText(acervoMod.getSub_titulo());
            this.texteFiedEdicao.setText(String.valueOf(acervoMod.getEdicao()));
            this.texteFiedVolume.setText(String.valueOf(acervoMod.getEdicao()));
            this.texteFiedNumPaginas.setText(String.valueOf(acervoMod.getNumero_paginas()));
            this.texteFieldAno.setText(String.valueOf(acervoMod.getAno_lancamento()));
            this.texteFiedCodigoBarra.setText(acervoMod.getCodigo_barra());
            this.texteFiedISBN.setText(acervoMod.getIsbn());
            this.textAreaSinopese.setText(acervoMod.getSinopse());
            comboBoxTipo.getSelectionModel().select(acervoMod.getTipo_acervo());
            comboBoxFormato.getSelectionModel().select(acervoMod.getFormato());
            comboBoxIdioma.getSelectionModel().select(acervoMod.getIdioma());
            for(int i=0; i<comboBoxEditora.getItems().size();i++){
                comboBoxEditora.getSelectionModel().select(i);
                if(acervoMod.getEditoraMod().getiEditora()== comboBoxEditora.getSelectionModel().getSelectedItem().getiEditora()){
                    break;
                }
            }
            for(int i=0; i<comboBoxAutor.getItems().size();i++){
                comboBoxAutor.getSelectionModel().select(i);
                if(acervoMod.getAutorMod().getIdAutor()== comboBoxAutor.getSelectionModel().getSelectedItem().getIdAutor()){
                    break;
                }
            }
            botaoAlterar.setDisable(false);
            botaoRemover.setDisable(false);
            this.desbloquearItensDaJanela();
            botaoNovo.setDisable(true);
            botaoCadastrar.setDisable(true);
        }else{
            botaoAlterar.setDisable(true);
            botaoRemover.setDisable(true);
            botaoNovo.setDisable(false);
            this.limparItensDaJanela();
        }
    }
    
    private ModAcervo pegarDadosDaPesquisa(){
        if(UtilValidarDados.eNumero(this.texteFiedPesquisar.getText())){
           acervoMod.setIdAcervo(Integer.valueOf(this.texteFiedPesquisar.getText()), operacao);
           acervoMod.setTitulo(this.texteFiedPesquisar.getText(), operacao);
           return acervoMod;
        }else{
           acervoMod.setTitulo(this.texteFiedPesquisar.getText(), operacao);
           return acervoMod;
        }
    }
    
    private void carregarResultadosNaTablea(List<Object> todosRegistosEncontrados){
        tableColumTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tableColumSubTitulo.setCellValueFactory(new PropertyValueFactory<>("sub_titulo"));
        tableColumEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        tableColumISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tableColumnAno.setCellValueFactory(new PropertyValueFactory<>("ano_lancamento"));
        tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo_acervo"));
        tableColumnFormato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        tableViewAcervo.setItems(this.todosRegistosParaCarregar(todosRegistosEncontrados));
    }
    
    private ObservableList<ModAcervo> todosRegistosParaCarregar(List<Object> todosRegistosEncontrados){
        List<ModAcervo> listaDosRegistosWncontrados = new ArrayList<>();
        for(Object acervoRegistado: todosRegistosEncontrados){
            ModAcervo acervoMod = (ModAcervo)acervoRegistado;
            listaDosRegistosWncontrados.add(acervoMod);
        } 
        return FXCollections.observableArrayList(listaDosRegistosWncontrados);
    }
    
    @FXML
    public void validarDadosNumericos(KeyEvent evt){
        String caracateresValidos = "1234567890";
        if(!caracateresValidos.contains(evt.getCharacter()+"")){
            evt.consume();
        }
    }

    
}
