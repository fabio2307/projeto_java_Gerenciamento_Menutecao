/*
 * The MIT License
 *
 * Copyright 2025 fabio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.prjmanutencao.telas;


import com.prjmanutencao.dal.ModuloConexao;
import static com.prjmanutencao.telas.TelaLogin.id;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import mondrian.util.MemoryMonitor.Test;
import net.proteanit.sql.DbUtils;

/**
 * Classe TelaCadastrarProduto
 *
 * Finalidade: Representa uma interface interna (JInternalFrame) para o cadastro
 * de produtos. Contém atributos e objetos necessários para manipulação de
 * dados, conexão com banco, controle de fluxo e armazenamento temporário de
 * arquivos.
 *
 * Atributos principais: - conexao: conexão com o banco de dados. - pst: comando
 * SQL preparado. - rs: resultado da consulta SQL. - Variáveis auxiliares: cod,
 * nf, sai, codSai, str, dataValid, pesquisa, tipo, setor, etc. - fis: fluxo de
 * entrada de arquivo para manipulação binária. - tamanho: tamanho do arquivo.
 *
 * Observações: - A classe depende de bibliotecas Swing e JDBC. - Pode ser
 * estendida com métodos para salvar, consultar, editar e excluir produtos.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaCadastrarProduto extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String cod;
    String nf;
    String sai;
    String codSai;
    String str;
    String dataValid = null;
    String dataValidCon = null;
    String pesquisa = null;
    String format;
    String list;
    String combo;
    String tipo;
    String setor;
    String tipoMod;
    String setorMod;
    int tatalProd = 0;
    int saida = 0;

    FileInputStream fis;
    int tamanho;

    /**
     * Construtor padrão da classe TelaCadastrarProduto.
     *
     * Este construtor realiza as seguintes ações: - Inicializa os componentes
     * da interface gráfica. - Executa a contagem inicial de produtos. - Ajusta
     * a configuração da tabela de produtos. - Define um renderizador
     * personalizado para a tabela de consulta de produtos. - Configura o modo
     * de seleção das tabelas para seleção única. - Obtém a data e hora atual no
     * formato curto e armazena na variável 'format'.
     */
    public TelaCadastrarProduto() {
        initComponents();
        contagem();
        ajustarTabela();
        tblProConsulta.setDefaultRenderer(Object.class, new alinhar());
        tblProConsulta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProdNotaFiscalCons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        ZonedDateTime zdtNow = ZonedDateTime.now();
        format = data_hora.format(zdtNow);
    }

    /**
     * Classe JlabelGradient.
     *
     * Esta classe estende JLabel para criar um rótulo com fundo em gradiente. O
     * gradiente é desenhado verticalmente, indo de uma cor azul translúcida
     * para branco, proporcionando um efeito visual suave.
     *
     * A principal personalização é feita sobrescrevendo o método
     * paintComponent, onde o gradiente é aplicado antes de qualquer outro
     * conteúdo do componente.
     *
     * Utiliza: - Graphics2D para renderização avançada. - GradientPaint para
     * criar o efeito de gradiente.
     *
     * Observação: Esta classe pode ser usada para melhorar a estética de
     * interfaces gráficas em aplicações Swing.
     */
    class JlabelGradient extends JLabel {

        @Override
        protected void paintComponent(Graphics estilo) {

            Graphics2D g2d = (Graphics2D) estilo;
            int width = getWidth();
            int height = getHeight();
            Color cor1 = new Color(109, 186, 255, 80);
            Color cor2 = new Color(255, 255, 255);
            GradientPaint gp = new GradientPaint(0, 0, cor1, 0, height, cor2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);

        }
    }

    /**
     * Classe DocumentSizeFilter.
     *
     * Esta classe estende DocumentFilter para limitar a quantidade máxima de
     * caracteres que podem ser inseridos ou substituídos em um campo de texto
     * (como JTextField ou JTextArea).
     *
     * Funcionalidade: - Impede que o usuário insira ou substitua texto além do
     * limite definido. - Emite um alerta sonoro (beep) quando o limite é
     * excedido. - Pode ser usada com documentos associados a componentes Swing
     * para controle de entrada.
     *
     * Construtor: DocumentSizeFilter(int maxChars) Define o número máximo de
     * caracteres permitidos.
     *
     * Métodos sobrescritos: - insertString: verifica se a inserção ultrapassa o
     * limite antes de permitir. - replace: verifica se a substituição
     * ultrapassa o limite antes de permitir.
     *
     * Observação: O campo DEBUG pode ser ativado para imprimir mensagens de
     * depuração durante a execução.
     */
    class DocumentSizeFilter extends DocumentFilter {

        int maxCharacters;
        boolean DEBUG = false;

        public DocumentSizeFilter(int maxChars) {
            maxCharacters = maxChars;
        }

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offs,
                String str, AttributeSet a)
                throws BadLocationException {
            if (DEBUG) {
                System.out.println("in DocumentSizeFilter's insertString method");
            }

            //Isto rejeita novas inserções de caracteres se
            //a string for muito grande. Outra opção seria
            //truncar a string inserida, de forma que seja
            //adicionado somente o necessário para atingir
            //o limite máximo permitido
            if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) {
                super.insertString(fb, offs, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
            if (DEBUG) {
                System.out.println("in DocumentSizeFilter's replace method");
            }
            //Isto rejeita novas inserções de caracteres se
            //a string for muito grande. Outra opção seria
            //truncar a string inserida, de forma que seja
            //adicionado somente o necessário para atingir
            //o limite máximo permitido
            if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters) {
                super.replace(fb, offs, length, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

    }

    /**
     * Método contagem.
     *
     * Este método inicializa documentos estilizados (DefaultStyledDocument) e
     * aplica filtros de tamanho máximo de caracteres usando a classe
     * DocumentSizeFilter.
     *
     * Cada documento é associado a um campo de texto da interface gráfica,
     * limitando a quantidade de caracteres que o usuário pode digitar.
     *
     * Campos afetados: - txaProdNome: limite de 50 caracteres -
     * txaProdFornecedor: limite de 50 caracteres - txaProdNotFiscal: limite de
     * 50 caracteres - txaProdDescricao: limite de 40 caracteres -
     * txaProdNomeCons: limite de 50 caracteres - txaProdForCons: limite de 50
     * caracteres - txaProdNotFiscalCons: limite de 50 caracteres -
     * txaProdDesCons: limite de 40 caracteres
     *
     * Utiliza: - DefaultStyledDocument para aplicar filtros personalizados. -
     * DocumentSizeFilter para restringir a entrada de texto.
     */
    private void contagem() {
        DefaultStyledDocument doc, doc1, doc2, doc3, doc4, doc5, doc6, doc7;

        doc = new DefaultStyledDocument();
        doc1 = new DefaultStyledDocument();
        doc2 = new DefaultStyledDocument();
        doc3 = new DefaultStyledDocument();
        doc4 = new DefaultStyledDocument();
        doc5 = new DefaultStyledDocument();
        doc6 = new DefaultStyledDocument();
        doc7 = new DefaultStyledDocument();

        doc.setDocumentFilter(new DocumentSizeFilter(50));
        doc1.setDocumentFilter(new DocumentSizeFilter(50));
        doc2.setDocumentFilter(new DocumentSizeFilter(50));
        doc3.setDocumentFilter(new DocumentSizeFilter(40));
        doc4.setDocumentFilter(new DocumentSizeFilter(50));
        doc5.setDocumentFilter(new DocumentSizeFilter(50));
        doc6.setDocumentFilter(new DocumentSizeFilter(50));
        doc7.setDocumentFilter(new DocumentSizeFilter(40));

        txaProdNome.setDocument(doc);
        txaProdFornecedor.setDocument(doc1);
        txaProdNotFiscal.setDocument(doc2);
        txaProdDescricao.setDocument(doc3);
        txaProdNomeCons.setDocument(doc4);
        txaProdForCons.setDocument(doc5);
        txaProdNotFiscalCons.setDocument(doc6);
        txaProdDesCons.setDocument(doc7);
    }

    /**
     * Classe alinhar.
     *
     * Esta classe estende DefaultTableCellRenderer para personalizar a
     * renderização de células em uma JTable. Ela centraliza horizontalmente o
     * conteúdo das células, ajusta a altura das linhas da tabela e define uma
     * fonte padrão.
     *
     * Funcionalidade: - Centraliza o texto ou componente dentro da célula. -
     * Define a altura da linha da tabela como 48 pixels. - Aplica a fonte
     * Arial, tamanho 12, estilo normal.
     *
     * Observação: A tabela utilizada (tblProConsulta) deve estar acessível no
     * escopo onde esta classe é aplicada, pois a altura da linha é definida
     * diretamente nela.
     */
    public class alinhar extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            tblProConsulta.setRowHeight(48);
            setFont(new Font("Arial", Font.PLAIN, 12));
            return label;

        }
    }

    /**
     * Método ajustarTabela.
     *
     * Configura a aparência da tabela tblProConsulta, ajustando larguras de
     * colunas, altura das linhas e estilo do cabeçalho.
     *
     * Funcionalidades: - Define a altura do cabeçalho da tabela como 30 pixels.
     * - Aplica fonte Arial em negrito, tamanho 12, ao cabeçalho. - Centraliza o
     * texto do cabeçalho horizontalmente. - Define larguras específicas para as
     * colunas: coluna 0: 90 pixels coluna 1: 240 pixels coluna 2: 90 pixels
     * coluna 3: 70 pixels - Define a altura das linhas da tabela como 22
     * pixels. - Chama o método ajustarTabelaNf() para aplicar configurações
     * adicionais.
     *
     * Tratamento de erro: - Exibe uma mensagem de erro em caso de falha na
     * configuração da tabela.
     */
    private void ajustarTabela() {

        int width = 90, width1 = 240, width2 = 90, width3 = 70;
        JTableHeader header;

        try {
            header = tblProConsulta.getTableHeader();
            header.setPreferredSize(new Dimension(0, 30));
            header.setFont(new Font("Arial", Font.BOLD, 12));
            header.setForeground(new Color(0, 0, 51));
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblProConsulta.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblProConsulta.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblProConsulta.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblProConsulta.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblProConsulta.setRowHeight(22);
            ajustarTabelaNf();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas" + e);
        }
    }

    /**
     * Método ajustarTabelaNf.
     *
     * Configura a aparência da tabela tblProdNotaFiscalCons, ajustando o
     * cabeçalho, larguras das colunas e estilo da fonte.
     *
     * Funcionalidades: - Define a altura do cabeçalho como 30 pixels. - Aplica
     * fonte Arial em negrito, tamanho 12, ao cabeçalho. - Define a cor do texto
     * do cabeçalho como azul escuro (RGB 0, 0, 51). - Centraliza o texto do
     * cabeçalho horizontalmente. - Define larguras específicas para as colunas:
     * coluna 0: 350 pixels coluna 1: 80 pixels - Aplica fonte Arial, tamanho
     * 11, ao conteúdo da tabela.
     *
     * Tratamento de erro: - Exibe uma mensagem de erro em caso de falha na
     * configuração da tabela.
     */
    private void ajustarTabelaNf() {

        int width = 350, width1 = 80;
        JTableHeader header;

        try {
            header = tblProdNotaFiscalCons.getTableHeader();
            header.setPreferredSize(new Dimension(0, 30));
            header.setFont(new Font("Arial", Font.BOLD, 12));
            header.setForeground(new Color(0, 0, 51));
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblProdNotaFiscalCons.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblProdNotaFiscalCons.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblProdNotaFiscalCons.setFont(new Font("Arial", Font.PLAIN, 11));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas NF" + e);
        }
    }

    /**
     * Método conferirImagem.
     *
     * Verifica se há uma imagem associada ao produto. Caso não haja, carrega
     * uma imagem padrão localizada no caminho
     * "/com/prjmanutencao/icones/produto_sem_imagem.png" e prepara os dados
     * para inserção no banco de dados.
     *
     * Funcionalidade: - Estabelece conexão com o banco de dados. - Verifica se
     * o fluxo de imagem (fis) está nulo e se o tamanho é zero. - Se não houver
     * imagem, carrega uma imagem padrão como fluxo de entrada
     * (FileInputStream). - Calcula o tamanho da imagem em bytes. - Chama o
     * método inserirProdutos() para prosseguir com o cadastro.
     *
     * Tratamento de exceções: - URISyntaxException: caso o caminho da imagem
     * não possa ser convertido para URI. - FileNotFoundException: caso o
     * arquivo da imagem padrão não seja encontrado. - Em ambos os casos, o erro
     * é registrado no log usando java.util.logging.
     *
     * Observação: Este método garante que sempre haverá uma imagem associada ao
     * produto, mesmo que seja uma imagem genérica.
     */
    private void conferirImagem() {

        conexao = ModuloConexao.conector();

        if (fis == null && tamanho == 0) {

            // Caminho relativo do arquivo da imagem
            URL imagePath = getClass().getResource("/com/prjmanutencao/icones/produto_sem_imagem.png");
            try {
                // Lê o arquivo da imagem como um array de bytes
                File imageFile;
                try {
                    imageFile = new File(imagePath.toURI());

                    fis = new FileInputStream(imageFile);
                    tamanho = (int) imageFile.length();
                    inserirProdutos();

                } catch (URISyntaxException ex) {
                    Logger.getLogger(TelaCadastrarProduto.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(TelaCadastrarProduto.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            inserirProdutos();
        }

    }

    /**
     * Método inserirProdutos.
     *
     * Realiza a inserção de um novo produto no banco de dados, incluindo seus
     * dados textuais e a imagem associada.
     *
     * Funcionalidades: - Obtém os dados dos campos da interface gráfica: nome,
     * tipo, quantidade, unidade de medida, setor e descrição do produto. -
     * Prepara uma instrução SQL para inserir os dados na tabela
     * 'cadastro_prod'. - Insere a imagem como um Blob usando o fluxo de entrada
     * 'fis' e o tamanho calculado. - Valida se os campos obrigatórios estão
     * preenchidos: - Nome do produto - Fornecedor - Nota fiscal - Quantidade
     * (deve ser maior que zero) - Emite um alerta sonoro e exibe uma mensagem
     * se houver campos obrigatórios vazios. - Executa a inserção no banco de
     * dados e, se bem-sucedida, chama o método 'codProduto()'.
     *
     * Tratamento de erro: - Captura e exibe exceções SQL usando JOptionPane.
     */
    private void inserirProdutos() {

        int comp;
        comp = (int) spnProdQuantidade.getValue();

        String sql = "insert into cadastro_prod (nomeProduto, tipoProduto, quantidadeProduto, unidadeMedida, setorProduto, descProduto, imagemProduto) values (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txaProdNome.getText());
            pst.setString(2, cboProdTipo.getSelectedItem().toString());
            pst.setInt(3, (int) spnProdQuantidade.getValue());
            pst.setString(4, cboProdUniMed.getSelectedItem().toString());
            pst.setString(5, cboProdSetor.getSelectedItem().toString());
            pst.setString(6, txaProdDescricao.getText());
            pst.setBlob(7, fis, tamanho);
            if ((txaProdNome.getText().isEmpty()) || (txaProdFornecedor.getText().isEmpty()) || (txaProdNotFiscal.getText().isEmpty()) || (comp == 0)) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios");
            } else {
                int adicionar = pst.executeUpdate();
                if (adicionar > 0) {
                    codProduto();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método codProduto.
     *
     * Recupera o código mais recente (máximo) do produto cadastrado na tabela
     * 'cadastro_prod' e armazena esse valor na variável 'cod'. Em seguida,
     * chama o método 'inserirNotaFiscal' para continuar o processo de cadastro.
     *
     * Funcionalidade: - Executa uma consulta SQL para obter o maior valor da
     * coluna 'cod'. - Armazena o resultado na variável 'cod' como String. -
     * Chama o método 'inserirNotaFiscal' para associar a nota fiscal ao produto
     * recém-cadastrado.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe uma mensagem de erro
     * usando JOptionPane.
     */
    private void codProduto() {

        String sql = "select max(cod) from cadastro_prod";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                cod = (rs.getString(1));
                inserirNotaFiscal();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método inserirNotaFiscal.
     *
     * Insere os dados da nota fiscal relacionados ao produto recém-cadastrado
     * na tabela 'produ_entrada'. Os dados incluem código do produto,
     * funcionário responsável, fornecedor, número da nota fiscal, data de
     * validade, data de entrada e quantidade.
     *
     * Funcionalidades: - Chama o método servicos() para obter a data de
     * validade formatada. - Prepara e executa a instrução SQL de inserção. - Se
     * a inserção for bem-sucedida, exibe uma mensagem de confirmação, limpa os
     * campos da interface e encerra a conexão com o banco de dados.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe uma mensagem de erro
     * via JOptionPane.
     */
    private void inserirNotaFiscal() {

        servicos();

        String sql = "insert into produ_entrada (cod, funEnt, nomeFornecedor, nfProduto, dataValid, dataEnt, quantidadeEnt, quantidadeTotal) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cod);
            pst.setString(2, id);
            pst.setString(3, txaProdFornecedor.getText());
            pst.setString(4, txaProdNotFiscal.getText());
            pst.setString(5, dataValid);
            pst.setString(6, format);
            pst.setInt(7, (int) spnProdQuantidade.getValue());
            pst.setInt(8, (int) spnProdQuantidade.getValue());
            int adicionar = pst.executeUpdate();
            if (adicionar > 0) {
                JOptionPane.showMessageDialog(null, "Produto Cadastrado com Sucesso");
                limpar();
                conexao.close();

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método servicos.
     *
     * Obtém a data selecionada no componente jdcProdDatValidade e a formata no
     * padrão "dd/MM/yyyy". O valor formatado é armazenado na variável
     * dataValid.
     *
     * Funcionalidade: - Utiliza SimpleDateFormat para aplicar o formato
     * desejado. - Extrai a data do componente gráfico e converte para String.
     *
     * Tratamento de erro: - Captura exceções genéricas e exibe uma mensagem de
     * erro via JOptionPane.
     */
    private void servicos() {

        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date pega;
            pega = jdcProdDatValidade.getDate();
            this.dataValid = formato.format(pega);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método carregarImag.
     *
     * Abre um seletor de arquivos para que o usuário escolha uma imagem (PNG,
     * JPG ou JPEG). Após a seleção, a imagem é lida, redimensionada para caber
     * no componente `lblProFoto`, e exibida como ícone nesse componente.
     *
     * Funcionalidades: - Usa JFileChooser para selecionar o arquivo. - Aplica
     * filtro para aceitar apenas imagens nos formatos especificados. - Lê o
     * arquivo como fluxo de entrada (FileInputStream) e calcula seu tamanho. -
     * Redimensiona a imagem com suavização (Image.SCALE_SMOOTH). - Atualiza o
     * componente gráfico com a imagem selecionada.
     *
     * Tratamento de erro: - Captura FileNotFoundException e IOException,
     * exibindo mensagens de erro apropriadas.
     *
     * Observação: - O campo `fis` armazena o fluxo de entrada da imagem, e
     * `tamanho` guarda o tamanho do arquivo. - O método depende do componente
     * `lblProFoto` para exibir a imagem.
     */
    private void carregarImag() {

        JFileChooser file = new JFileChooser();
        file.setDialogTitle("Selelecionar Imagem");
        file.setFileFilter(new FileNameExtensionFilter("Arquivo de Imagem (*.PNG, *.JPG, *.JPEG)", "png", "jpg", "jpeg"));
        int resultado = file.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                fis = new FileInputStream(file.getSelectedFile());
                tamanho = (int) file.getSelectedFile().length();
                Image foto = ImageIO.read(file.getSelectedFile()).getScaledInstance(lblProFoto.getWidth(), lblProFoto.getHeight(), Image.SCALE_SMOOTH);
                lblProFoto.setIcon(new ImageIcon(foto));
                lblProFoto.updateUI();

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e);
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método pesquisarProd.
     *
     * Realiza a busca de produtos no banco de dados com base no texto digitado
     * pelo usuário e no filtro de setor (caso não seja "Todos"). Os resultados
     * são exibidos em uma JTable com imagens renderizadas.
     *
     * Funcionalidades: - Limpa os campos de consulta de fornecedor e nota
     * fiscal. - Se o filtro de pesquisa for "Todos", busca por código ou nome
     * do produto. - Caso contrário, busca por código ou nome do produto dentro
     * de um setor específico. - Cria um modelo de tabela com colunas para
     * código, nome, quantidade e imagem. - Define um renderizador personalizado
     * para exibir imagens na tabela. - Adiciona os resultados da consulta ao
     * modelo da tabela. - Chama o método `ajustarTabela()` para ajustar a
     * exibição da JTable.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * JOptionPane.
     *
     * Observações: - O método depende de componentes gráficos como
     * `txtProdPesquisar`, `tblProConsulta`, e `ImageRenderer1` para renderizar
     * imagens. - A conexão com o banco é encerrada após a execução da consulta.
     */
    private void pesquisarProd() {

        txaProdForCons.setText("");
        txaProdNotFiscalCons.setText("");

        if (pesquisa.equals("Todos")) {
            conexao = ModuloConexao.conector();

            String sql = "select cod, nomeProduto, quantidadeProduto, imagemProduto from cadastro_prod where cod like ? or nomeProduto like ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtProdPesquisar.getText() + "%");
                pst.setString(2, txtProdPesquisar.getText() + "%");
                rs = pst.executeQuery();
                // Cria um DefaultTableModel e adiciona uma coluna para as imagens
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Código");
                model.addColumn("Produto");
                model.addColumn("Quantidade");
                model.addColumn("Imagem");

                // Cria um JTable e define o seu modelo
                tblProConsulta.setModel(model);

                // Define um renderizador personalizado para a coluna da imagem
                tblProConsulta.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer1());

                // Recupera e adiciona as imagens ao modelo
                while (rs.next()) {
                    byte[] imgBytes = rs.getBytes(4);
                    model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        new ImageIcon(imgBytes)
                    });
                    ajustarTabela();
                }
                conexao.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        } else {
            conexao = ModuloConexao.conector();
            ajustarTabela();
            String sql = "select cod, nomeProduto, quantidadeProduto, imagemProduto from cadastro_prod where setorProduto = ? and cod like ? or setorProduto = ? and nomeProduto like ? ";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, pesquisa);
                pst.setString(2, txtProdPesquisar.getText() + "%");
                pst.setString(3, pesquisa);
                pst.setString(4, txtProdPesquisar.getText() + "%");
                rs = pst.executeQuery();
                ajustarTabela();
                // Cria um DefaultTableModel e adiciona uma coluna para as imagens
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Código");
                model.addColumn("Produto");
                model.addColumn("Quantidade");
                model.addColumn("Imagem");

                // Cria um JTable e define o seu modelo
//            tblTeste = new JTable(model);
                tblProConsulta.setModel(model);

                // Define um renderizador personalizado para a coluna da imagem
                tblProConsulta.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer1());

                // Recupera e adiciona as imagens ao modelo
                while (rs.next()) {
//                tblTeste.add(rs.getString(1));
                    byte[] imgBytes = rs.getBytes(4);
                    model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        new ImageIcon(imgBytes)
                    });
                    ajustarTabela();
                }
                conexao.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Método setProd.
     *
     * Recupera os dados da linha selecionada na tabela `tblProConsulta` e os
     * aplica aos campos da interface gráfica, permitindo que o usuário
     * visualize ou edite os detalhes do produto.
     *
     * Funcionalidades: - Obtém o índice da linha selecionada na tabela. -
     * Define o campo de código do produto (`txtProdCodCon`) com o valor da
     * primeira coluna. - Chama os métodos auxiliares `cboSelectCons()`,
     * `prodPreecher()` e `verificacaoCons()` para preencher os demais campos e
     * realizar verificações adicionais.
     *
     * Observações: - Este método depende da seleção de uma linha válida na
     * tabela. - Os métodos chamados devem estar corretamente implementados para
     * garantir o funcionamento completo.
     */
    private void setProd() {
        int setar = tblProConsulta.getSelectedRow();
        txtProdCodCon.setText(tblProConsulta.getModel().getValueAt(setar, 0).toString());
        cboSelectCons();
        prodPreecher();
        verificacaoCons();
    }

    /**
     * Método cboSelectCons.
     *
     * Atualiza os itens do componente `cboProdSetorCons` com base no valor da
     * variável `combo`. Se `combo` for "Tipo", carrega categorias de produto.
     * Se for "Setor", carrega setores operacionais.
     *
     * Funcionalidades: - Cria um novo modelo de combo box vazio
     * (`DefaultComboBoxModel`). - Adiciona itens específicos conforme o tipo de
     * seleção. - Exibe uma mensagem de erro se o valor de `combo` não
     * corresponder a nenhuma opção esperada.
     *
     * Observações: - O método depende da variável `combo` estar corretamente
     * definida antes da execução. - Os itens adicionados são fixos e podem ser
     * adaptados conforme a necessidade da aplicação.
     */
    private void cboSelectCons() {

        switch (combo) {
            case "Tipo":
                cboProdSetorCons.setModel(new DefaultComboBoxModel());
                String cbo[] = {"Alimentação", "Papelaria", "Manutenção", "Limpeza"};

                for (String string : cbo) {
                    cboProdSetorCons.addItem(string);
                }
                break;
            case "Setor":
                cboProdSetorCons.setModel(new DefaultComboBoxModel());
                String cbo1[] = {"A e B (Alimentos e Bebidas)", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança"};
                for (String string : cbo1) {
                    cboProdSetorCons.addItem(string);
                }
                break;

            default:
                JOptionPane.showMessageDialog(null, "Nenhuma Opção Encontada");
        }
    }

    /**
     * Método verificacaoCons.
     *
     * Realiza a verificação do campo `txtProdCodCon` para determinar se há um
     * produto selecionado e, com base nisso, ajusta o estado dos botões e
     * campos da interface gráfica.
     *
     * Funcionalidades: - Se o campo de código do produto não estiver nulo,
     * ativa botões de edição e desativa campos relacionados ao cadastro
     * original do produto. - Habilita campos específicos para edição
     * complementar ou consulta.
     *
     * Observações: - Este método pressupõe que `txtProdCodCon` já foi
     * preenchido, geralmente após seleção na tabela. - Não verifica se o campo
     * está vazio, apenas se é diferente de `null`. - Pode ser aprimorado com
     * validação adicional usando `!veriicar.isEmpty()` para maior segurança.
     */
    private void verificacaoCons() {

        String veriicar;
        veriicar = txtProdCodCon.getText();

        if (veriicar != null) {
            btnProdAdd.setEnabled(false);
            btnProdAlte.setEnabled(true);
            btnProdApagar.setEnabled(true);
            btnProdArquivoInicio.setEnabled(true);
            btnProdNovo.setEnabled(true);
            btnProdModFoto.setEnabled(true);
            jdcProdDatValidade.setEnabled(false);
            txaProdNome.setEditable(false);
            txaProdFornecedor.setEditable(false);
            txaProdNotFiscal.setEditable(false);
            txaProdDescricao.setEditable(false);
            cboProdSetor.setEnabled(false);
            cboProdTipo.setEnabled(false);
            cboProdUniMed.setEnabled(false);
            spnProdQuantidade.setEnabled(false);

            txaProdNomeCons.setEditable(true);
            txaProdDesCons.setEditable(true);
            cboProdSetorCons.setEnabled(true);
            cboProdUnidMedidaCons.setEnabled(true);
            spnProdAdd.setEnabled(true);
        }
    }

    /**
     * Método servicosCon.
     *
     * Obtém a data selecionada no componente `jdcProdDatValidadeCons` e a
     * formata no padrão "dd/MM/yyyy". O valor formatado é armazenado na
     * variável `dataValidCon`.
     *
     * Funcionalidades: - Utiliza `SimpleDateFormat` para aplicar o formato
     * desejado. - Extrai a data do componente gráfico e converte para `String`.
     *
     * Tratamento de erro: - Captura exceções genéricas (por exemplo, se nenhuma
     * data estiver selecionada) e exibe uma mensagem de erro via `JOptionPane`.
     *
     * Observações: - Este método é útil para garantir consistência no formato
     * de data antes de salvar ou exibir. - Pode ser aprimorado com validação
     * explícita para verificar se a data foi realmente selecionada.
     */
    private void servicosCon() {

        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date pega;
            pega = jdcProdDatValidadeCons.getDate();
            this.dataValidCon = formato.format(pega);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método prodPreecher.
     *
     * Preenche os campos da interface gráfica com os dados de um produto
     * específico, recuperado do banco de dados com base no código informado em
     * `txtProdCodCon`.
     *
     * Funcionalidades: - Estabelece conexão com o banco de dados. - Executa uma
     * consulta SQL para obter nome, quantidade, unidade de medida, tipo, setor
     * e descrição do produto. - Preenche os campos correspondentes na interface
     * com os dados obtidos. - Atualiza variáveis auxiliares como `tipo` e
     * `setor`. - Chama o método `saidaProdutosNf()` para realizar ações
     * adicionais após o preenchimento.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método executa `pst.executeQuery()` duas vezes, o que é
     * redundante e pode ser removido. - A conexão com o banco não é
     * explicitamente fechada dentro do método. - Pode ser aprimorado com
     * validação para garantir que o campo de código não esteja vazio.
     */
    private void prodPreecher() {

        conexao = ModuloConexao.conector();

        String sql = "select nomeProduto, quantidadeProduto, unidadeMedida, tipoProduto, setorProduto, descProduto from cadastro_prod where cod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodCon.getText());
            rs = pst.executeQuery();
            rs = pst.executeQuery();
            if (rs.next()) {
                txaProdNomeCons.setText(rs.getString(1));
                txtProdQuantCons.setText(rs.getString(2));
                cboProdUnidMedidaCons.setSelectedItem(rs.getString(3));
                tipo = (rs.getString(4));
                cboProdSetorCons.setSelectedItem(tipo);
                setor = (rs.getString(5));
                txaProdDesCons.setText(rs.getString(6));
            }
            saidaProdutosNf();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método prodPreecher.
     *
     * Preenche os campos da interface gráfica com os dados de um produto
     * específico, recuperado do banco de dados com base no código informado em
     * `txtProdCodCon`.
     *
     * Funcionalidades: - Estabelece conexão com o banco de dados. - Executa uma
     * consulta SQL para obter nome, quantidade, unidade de medida, tipo, setor
     * e descrição do produto. - Preenche os campos correspondentes na interface
     * com os dados obtidos. - Atualiza variáveis auxiliares como `tipo` e
     * `setor`. - Chama o método `saidaProdutosNf()` para realizar ações
     * adicionais após o preenchimento.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método executa `pst.executeQuery()` duas vezes, o que é
     * redundante e pode ser removido. - A conexão com o banco não é
     * explicitamente fechada dentro do método. - Pode ser aprimorado com
     * validação para garantir que o campo de código não esteja vazio.
     */
    private void comparar() {

        conexao = ModuloConexao.conector();

        int comp;
        comp = (int) spnProdAdd.getValue();

        if (comp > 0) {
            jdcProdDatValidadeCons.setEnabled(true);
            txaProdForCons.setEditable(true);
            txaProdNotFiscalCons.setEditable(true);
            btnProdApagar.setEnabled(false);
            ((DefaultTableModel) tblProdNotaFiscalCons.getModel()).setRowCount(0);
        } else {
            jdcProdDatValidadeCons.setEnabled(false);
            txaProdForCons.setEditable(false);
            txaProdNotFiscalCons.setEditable(false);
            txaProdForCons.setText("");
            txaProdNotFiscalCons.setText("");
            btnProdApagar.setEnabled(true);
            saidaProdutosNf();
        }

    }

    /**
     * Método produtoCalculo.
     *
     * Realiza o cálculo da nova quantidade total de um produto com base nas
     * operações de entrada (adição) e saída (retirada), atualizando o valor em
     * `tatalProd` e chamando o método `modifiacarProdutos()` para aplicar as
     * alterações.
     *
     * Funcionalidades: - Obtém a quantidade atual do produto a partir do campo
     * `txtProdQuantCons`. - Recupera os valores dos spinners `spnProdAdd` e
     * `spnProdRetirar`. - Se houver adição (`addPro > 0`), soma ao total e
     * verifica se os campos obrigatórios estão preenchidos. - Se houver
     * retirada (`saiPro > 0`), subtrai do total e aplica a modificação. - Se
     * não houver alteração, mantém o valor atual e ainda assim chama
     * `modifiacarProdutos()`.
     *
     * Tratamento de erro: - Exibe uma mensagem de alerta se os campos
     * obrigatórios estiverem vazios durante uma adição.
     *
     * Observações: - O método assume que os valores dos campos são válidos e
     * convertíveis para inteiros. - Pode ser aprimorado com validações
     * adicionais para evitar valores negativos ou inconsistentes.
     */
    private void produtoCalculo() {

        conexao = ModuloConexao.conector();
        String total;
        total = txtProdQuantCons.getText();
        int prodAtual;
        int addPro;
        int saiPro;

        addPro = (int) spnProdAdd.getValue();
        saiPro = (int) spnProdRetirar.getValue();
        prodAtual = Integer.valueOf(total);

        if (addPro > 0) {

            tatalProd = prodAtual + addPro;
            if ((txaProdNomeCons.getText().isEmpty()) || (txaProdNotFiscalCons.getText().isEmpty()) || (txaProdForCons.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preecha os Campos Obrigatórios");
                txaProdForCons.requestFocus();
            } else {
                modifiacarProdutos();
            }

        }
        if (saiPro > 0) {
            tatalProd = prodAtual - saiPro;
            modifiacarProdutos();
        }
        if (saiPro == 0 && addPro == 0) {
            tatalProd = prodAtual;
            modifiacarProdutos();
        }
    }

    /**
     * Método verificacao.
     *
     * Verifica se os campos obrigatórios do formulário de cadastro de produto
     * estão preenchidos. Se algum dos campos estiver vazio, desabilita os
     * botões de ação relacionados. Caso contrário, habilita os botões para
     * permitir a continuação do processo.
     *
     * Funcionalidades: - Verifica se os campos `txaProdNome`,
     * `txaProdFornecedor` e `txaProdNotFiscal` estão vazios. - Controla o
     * estado dos botões `btnProdAdd` e `btnProdArquivoInicio` com base nessa
     * verificação.
     *
     * Observações: - Este método é útil para garantir que o usuário preencha os
     * campos essenciais antes de prosseguir. - Pode ser expandido para incluir
     * validações adicionais, como formato de dados ou consistência entre
     * campos.
     */
    private void verificacao() {

        if ((txaProdNome.getText().isEmpty()) || (txaProdFornecedor.getText().isEmpty()) || (txaProdNotFiscal.getText().isEmpty())) {
            btnProdAdd.setEnabled(false);
            btnProdArquivoInicio.setEnabled(false);
        } else {
            btnProdAdd.setEnabled(true);
            btnProdArquivoInicio.setEnabled(true);
        }

    }

    /**
     * Método atualizarImagem.
     *
     * Atualiza a imagem de um produto no banco de dados com base no código
     * informado. A imagem é enviada como um `InputStream` (armazenado em `fis`)
     * e gravada como um `BLOB`.
     *
     * Funcionalidades: - Estabelece conexão com o banco de dados. - Prepara e
     * executa uma instrução SQL para atualizar o campo `imagemProduto`. -
     * Verifica se os dados da imagem e o código do produto estão válidos. -
     * Exibe uma mensagem de erro se a imagem não for selecionada ou o código
     * estiver vazio. - Se a atualização for bem-sucedida, exibe uma mensagem de
     * confirmação e atualiza o ícone da interface. - Limpa os dados da imagem
     * (`fis` e `tamanho`) após a operação.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como `fis`,
     * `tamanho`, `txtProdCodCon`, e `lblProFoto`. - A verificação de imagem
     * ocorre **após** a execução da query, o que pode ser invertido para
     * otimização. - Pode ser aprimorado com validações mais robustas e
     * tratamento de cache para exibição da nova imagem.
     */
    private void atualizarImagem() {

        conexao = ModuloConexao.conector();
        String sql = "update cadastro_prod set imagemProduto = ? where cod = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setBlob(1, fis, tamanho);
            pst.setString(2, txtProdCodCon.getText());
            int adicionar = pst.executeUpdate();
            if (fis == null && tamanho == 0 || (txtProdCodCon.getText().isEmpty())) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Escolha uma Imagem!");
            } else {
                if (adicionar > 0) {
                    JOptionPane.showMessageDialog(null, "Imagem Atualizada");
                    ImageIcon icone = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/imageFoto.png"));
                    Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblProFoto.getWidth(), lblProFoto.getHeight(), Image.SCALE_SMOOTH));
                    lblProFoto.setIcon(foto);
                    fis = null;
                    tamanho = 0;
                }
                conexao.close();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método modifiacarProdutos.
     *
     * Atualiza os dados de um produto no banco de dados com base no código
     * informado. Os campos atualizados incluem nome, quantidade, unidade de
     * medida, tipo, setor e descrição.
     *
     * Funcionalidades: - Prepara uma instrução SQL `UPDATE` para modificar os
     * dados do produto na tabela `cadastro_prod`. - Define os parâmetros da
     * query com os valores obtidos da interface gráfica. - Executa a
     * atualização e, se bem-sucedida, chama o método `compEntradaSaida()` para
     * ações complementares.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como `conexao`,
     * `pst`, `tatalProd`, `tipoMod`, `setorMod`, e componentes gráficos como
     * `txaProdNomeCons`, `cboProdUnidMedidaCons`, `txtProdCodCon`, etc. - A
     * conexão com o banco não é explicitamente fechada, o que pode ser
     * melhorado. - O nome do método contém um erro de digitação ("modifiacar"
     * em vez de "modificar").
     */
    private void modifiacarProdutos() {

        String sql = "update cadastro_prod set nomeProduto = ?, quantidadeProduto = ?, unidadeMedida = ?, tipoProduto = ?, setorProduto = ?, descProduto = ? where cod = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txaProdNomeCons.getText());
            pst.setInt(2, tatalProd);
            pst.setString(3, cboProdUnidMedidaCons.getSelectedItem().toString());
            pst.setString(4, tipoMod);
            pst.setString(5, setorMod);
            pst.setString(6, txaProdDesCons.getText());
            pst.setString(7, txtProdCodCon.getText());
            int adicionar = pst.executeUpdate();
            if (adicionar > 0) {
                compEntradaSaida();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método compEntradaSaida.
     *
     * Realiza o controle de entrada e saída de produtos com base nos valores
     * informados nos spinners `spnProdAdd` (adição) e `spnProdRetirar`
     * (retirada). Executa ações específicas conforme o tipo de operação.
     *
     * Funcionalidades: - Se houver adição (`addPro > 0`), chama o método
     * `novaNf()` para registrar a nova nota fiscal. - Se houver retirada
     * (`saiPro > 0`), chama o método `atualizacaoSaidaProd()` para atualizar a
     * saída do produto. - Se não houver alteração, exibe uma mensagem de
     * confirmação e chama `limparCons()` para limpar os campos. - Fecha a
     * conexão com o banco de dados após a operação.
     *
     * Tratamento de erro: - Captura exceções SQL ao fechar a conexão e registra
     * no log via `Logger`.
     *
     * Observações: - O método depende de variáveis externas como `spnProdAdd`,
     * `spnProdRetirar`, e `conexao`. - Pode ser aprimorado com validações
     * adicionais para evitar operações inconsistentes.
     */
    private void compEntradaSaida() {

        conexao = ModuloConexao.conector();
        int addPro;
        int saiPro;

        addPro = (int) spnProdAdd.getValue();
        saiPro = (int) spnProdRetirar.getValue();

        if (addPro > 0) {
            novaNf();
        }
        if (saiPro > 0) {
            atualizacaoSaidaProd();
        }
        if (saiPro == 0 && addPro == 0) {
            JOptionPane.showMessageDialog(null, "Cadastro Atualizado!");
            limparCons();
            try {
                conexao.close();
            } catch (SQLException ex) {
                Logger.getLogger(TelaCadastrarProduto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Método novaNf.
     *
     * Insere uma nova nota fiscal de entrada de produto na tabela
     * `produ_entrada` do banco de dados. Os dados incluem código do produto,
     * funcionário responsável, fornecedor, número da nota fiscal, data de
     * validade, data de entrada, quantidade adicionada e quantidade total.
     *
     * Funcionalidades: - Chama o método `servicosCon()` para obter a data de
     * validade formatada. - Prepara e executa uma instrução SQL `INSERT` com os
     * dados coletados da interface. - Verifica se os campos obrigatórios
     * (`txaProdForCons`, `txaProdNotFiscalCons`) estão preenchidos. - Exibe
     * mensagens de erro ou sucesso conforme o resultado da operação. - Limpa os
     * campos da interface e fecha a conexão com o banco após inserção
     * bem-sucedida.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - A verificação de campos obrigatórios ocorre após a
     * execução da query, o que pode ser invertido para evitar operações
     * desnecessárias. - O método depende de variáveis externas como `id`,
     * `format`, `dataValidCon`, `conexao`, e componentes gráficos.
     */
    private void novaNf() {

        servicosCon();
        String sql = "insert into produ_entrada (cod, funEnt, nomeFornecedor, nfProduto, dataValid, dataEnt, quantidadeEnt, quantidadeTotal) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodCon.getText());
            pst.setString(2, id);
            pst.setString(3, txaProdForCons.getText());
            pst.setString(4, txaProdNotFiscalCons.getText());
            pst.setString(5, dataValidCon);
            pst.setString(6, format);
            pst.setInt(7, (int) spnProdAdd.getValue());
            pst.setInt(8, (int) spnProdAdd.getValue());
            int adicionar = pst.executeUpdate();
            if ((txaProdForCons.getText().isEmpty()) || (txaProdNotFiscalCons.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preecha os Campos Obrigatórios");
                txaProdForCons.requestFocus();
            } else {
                if (adicionar > 0) {
                    JOptionPane.showMessageDialog(null, "Produto Adicionado com Sucesso!");
                    limparCons();
                    conexao.close();
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método saidaProdutosNf.
     *
     * Exibe as notas fiscais de entrada associadas a um produto específico,
     * desde que ainda haja quantidade disponível (`quantidadeTotal > 0`). Os
     * dados são carregados em uma tabela da interface.
     *
     * Funcionalidades: - Chama o método `cboSelectCons()` para configurar o
     * combo box de setores/tipos. - Executa uma consulta SQL que retorna o
     * número da nota fiscal e a data de validade para o produto identificado
     * por `txtProdCodCon`. - Aplica o resultado da consulta ao modelo da tabela
     * `tblProdNotaFiscalCons` usando `DbUtils`. - Chama `ajustarTabela()` para
     * ajustar a exibição da tabela. - Encerra a conexão com o banco de dados
     * após a operação.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como
     * `txtProdCodCon`, `tblProdNotaFiscalCons`, e `conexao`. - Pode ser
     * aprimorado com validações para garantir que o código do produto esteja
     * preenchido antes da consulta.
     */
    private void saidaProdutosNf() {

        cboSelectCons();

        String sql = "select nfProduto as 'Nota Fiscal', dataValid as 'Validade' from produ_entrada where cod = ? and quantidadeTotal > 0";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodCon.getText());
            rs = pst.executeQuery();
            tblProdNotaFiscalCons.setModel(DbUtils.resultSetToTableModel(rs));
            ajustarTabela();
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método setNotaFiscal.
     *
     * Recupera o número da nota fiscal da linha selecionada na tabela
     * `tblProdNotaFiscalCons` e armazena esse valor na variável `list`. Em
     * seguida, chama o método `numeroNf()` para processar ou exibir os dados
     * relacionados à nota fiscal.
     *
     * Funcionalidades: - Obtém o índice da linha selecionada na tabela. -
     * Extrai o valor da primeira coluna (presumivelmente o número da nota
     * fiscal). - Armazena esse valor na variável `list`. - Chama o método
     * `numeroNf()` para ações complementares. - Desabilita o botão
     * `btnProdApagar` para evitar exclusões enquanto a nota está sendo
     * manipulada.
     *
     * Observações: - O método depende da seleção de uma linha válida na tabela.
     * - Pode ser aprimorado com validação para garantir que uma linha foi
     * realmente selecionada antes de acessar os dados.
     */
    private void setNotaFiscal() {

        int setar = tblProdNotaFiscalCons.getSelectedRow();
        list = (tblProdNotaFiscalCons.getModel().getValueAt(setar, 0).toString());
        numeroNf();
        btnProdApagar.setEnabled(false);

    }

    /**
     * Método numeroNf.
     *
     * Recupera os dados de uma nota fiscal específica associada a um produto,
     * com base no número da nota (`list`) e no código do produto
     * (`txtProdCodCon`). Os dados são usados para preencher os campos da
     * interface gráfica e configurar os limites de retirada de produto.
     *
     * Funcionalidades: - Executa uma consulta SQL para obter o código da nota
     * (`nfCod`), quantidade disponível (`quantidadeTotal`), data de validade,
     * nome do fornecedor e número da nota fiscal. - Preenche os campos da
     * interface com os dados obtidos. - Converte a data de validade de `String`
     * para `Date` usando `SimpleDateFormat`. - Configura o spinner
     * `spnProdRetirar` com um modelo que limita a retirada à quantidade
     * disponível. - Desabilita o spinner de adição (`spnProdAdd`) e outros
     * campos de edição para evitar alterações indevidas.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`. - Captura exceções de parsing de data (`ParseException`) e
     * registra no log via `Logger`.
     *
     * Observações: - O método depende de variáveis externas como `list`,
     * `txtProdCodCon`, `spnProdRetirar`, `spnProdAdd`,
     * `jdcProdDatValidadeCons`, `txaProdForCons`, `txaProdNotFiscalCons`, e
     * `conexao`. - Pode ser aprimorado com validação para garantir que `list` e
     * `txtProdCodCon` estejam preenchidos antes da consulta.
     */
    private void numeroNf() {
        conexao = ModuloConexao.conector();
        int limite = 0;

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date data;

        String sql = "select nfCod, quantidadeTotal, dataValid, nomeFornecedor, nfProduto from produ_entrada where nfProduto = ? and cod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, list);
            pst.setString(2, txtProdCodCon.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                codSai = (rs.getString(1));
                spnProdRetirar.setValue(rs.getInt(2));
                saida = (rs.getInt(2));
                limite = (rs.getInt(2));
                data = formato.parse((rs.getString(3)));
                jdcProdDatValidadeCons.setDate(data);
                txaProdForCons.setText(rs.getString(4));
                txaProdNotFiscalCons.setText(rs.getString(5));
            }
            spnProdRetirar.setModel(new SpinnerNumberModel(limite, 0, limite, 1));
            spnProdAdd.setEnabled(false);
            spnProdAdd.setValue(0);
            spnProdRetirar.setEnabled(true);

            txaProdNomeCons.setEditable(false);
            txaProdDesCons.setEditable(false);
            cboProdSetorCons.setEnabled(false);
            cboProdUnidMedidaCons.setEnabled(false);
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (ParseException ex) {
            Logger.getLogger(TelaCadastrarProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizacaoSaidaProd() {

        conexao = ModuloConexao.conector();

        int saidTotal;
        int quandSaida;

        quandSaida = (int) spnProdRetirar.getValue();
        saidTotal = saida - quandSaida;

        String sql = "update produ_entrada set quantidadeSai = ?, quantidadeTotal = ? where cod = ? and nfCod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, (int) spnProdRetirar.getValue());
            pst.setInt(2, saidTotal);
            pst.setString(3, txtProdCodCon.getText());
            pst.setString(4, codSai);
            int adicionar = pst.executeUpdate();
            if (adicionar > 0) {
                saidaProdutos();
            }
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método saidaProdutos.
     *
     * Registra a saída de um produto no banco de dados, vinculando-a a uma nota
     * fiscal existente. A quantidade registrada é calculada com base na
     * diferença entre o valor original (`saida`) e o valor informado no spinner
     * `spnProdRetirar`.
     *
     * Funcionalidades: - Estabelece conexão com o banco de dados. - Calcula a
     * quantidade restante após a retirada. - Prepara e executa uma instrução
     * SQL `INSERT` na tabela `produ_saida`, incluindo: - Data da saída
     * (`format`) - Identificador do funcionário (`id`) - Código do produto -
     * Quantidade retirada - Código da nota fiscal (`codSai`) - Exibe uma
     * mensagem de sucesso se a operação for concluída. - Chama o método
     * `limparCons()` para limpar os campos da interface. - Encerra a conexão
     * com o banco de dados.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como `format`,
     * `id`, `txtProdCodCon`, `codSai`, `saida`, e `conexao`. - Pode ser
     * aprimorado com validações para garantir que a quantidade retirada seja
     * válida e não exceda o estoque.
     */
    private void saidaProdutos() {

        conexao = ModuloConexao.conector();

        int saidTotal;
        int quandSaida;

        quandSaida = (int) spnProdRetirar.getValue();
        saidTotal = saida - quandSaida;

        String sql = "insert into produ_saida (dataSai, idSai, cod, quantidadeSaid, nfCod) values (?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, format);
            pst.setString(2, id);
            pst.setString(3, txtProdCodCon.getText());
            pst.setInt(4, saidTotal);
            pst.setString(5, codSai);
            int adicionar = pst.executeUpdate();
            if (adicionar > 0) {
                JOptionPane.showMessageDialog(null, "Saida de Produtro Registrado com Sucesso");
                limparCons();
                conexao.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método verificarQuantidadeExcluir.
     *
     * Verifica se a quantidade de um produto no estoque é igual a zero antes de
     * permitir sua exclusão. Caso não haja mais unidades disponíveis, chama o
     * método `excluiPodruSai()` para realizar a exclusão. Se ainda houver
     * produtos em estoque, exibe uma mensagem de alerta e impede a exclusão.
     *
     * Funcionalidades: - Estabelece conexão com o banco de dados. - Executa uma
     * consulta SQL para obter a quantidade atual do produto com base no código
     * informado. - Compara o valor retornado com zero. - Se for igual a zero,
     * chama o método de exclusão. - Caso contrário, exibe uma mensagem
     * informando que a exclusão não é permitida.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como
     * `txtProdCodCon`, `conexao`, e do método `excluiPodruSai()`. - Pode ser
     * aprimorado com validações adicionais para garantir que o código do
     * produto esteja preenchido antes da consulta.
     */
    private void verificarQuantidadeExcluir() {

        conexao = ModuloConexao.conector();
        String quantidade;

        String sql = "select quantidadeProduto from cadastro_prod where cod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodCon.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                quantidade = (rs.getString(1));
                if (quantidade.equals("0")) {
                    excluiPodruSai();
                } else {
                    JOptionPane.showMessageDialog(null, "Ainda a Produtos no Estoque!" + "\n" + "Não é Possivel Excluir este Registro!", "Atenção", 2);
                    conexao.close();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método excluiPodruSai.
     *
     * Exclui os registros de saída de um produto específico da tabela
     * `produ_saida` com base no código informado em `txtProdCodCon`. Após a
     * exclusão, chama o método `excluiPodruEnt()` para remover os registros de
     * entrada correspondentes.
     *
     * Funcionalidades: - Prepara uma instrução SQL `DELETE` para remover os
     * dados da tabela `produ_saida`. - Executa a exclusão com base no código do
     * produto. - Se ao menos um registro for excluído, chama o método
     * `excluiPodruEnt()` para completar a remoção.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como
     * `txtProdCodCon`, `conexao`, e do método `excluiPodruEnt()`. - O nome do
     * método contém um erro de digitação ("excluiPodruSai" em vez de
     * "excluirProdutoSaida"). Recomenda-se renomear para refletir melhor sua
     * função.
     */
    private void excluiPodruSai() {

        String sql = "delete from produ_saida where cod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodCon.getText());
            int apagado = pst.executeUpdate();
            if (apagado > 0) {
                excluiPodruEnt();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método excluiPodruEnt.
     *
     * Exclui os registros de entrada de um produto específico da tabela
     * `produ_entrada` com base no código informado em `txtProdCodCon`. Após a
     * exclusão, chama o método `excluiPodru()` para remover o cadastro
     * principal do produto.
     *
     * Funcionalidades: - Prepara uma instrução SQL `DELETE` para remover os
     * dados da tabela `produ_entrada`. - Executa a exclusão com base no código
     * do produto. - Se ao menos um registro for excluído, chama o método
     * `excluiPodru()` para completar a remoção.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como
     * `txtProdCodCon`, `conexao`, e do método `excluiPodru()`. - O nome do
     * método contém um erro de digitação ("excluiPodruEnt" em vez de
     * "excluirProdutoEntrada"). Recomenda-se renomear para refletir melhor sua
     * função.
     */
    private void excluiPodruEnt() {

        String sql = "delete from produ_entrada where cod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodCon.getText());
            int apagado = pst.executeUpdate();
            if (apagado > 0) {
                excluiPodru();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método excluiPodru.
     *
     * Exclui o registro principal de um produto da tabela `cadastro_prod` com
     * base no código informado em `txtProdCodCon`. Após a exclusão, exibe uma
     * mensagem de confirmação e limpa os campos da interface.
     *
     * Funcionalidades: - Estabelece conexão com o banco de dados. - Prepara e
     * executa uma instrução SQL `DELETE` para remover o produto. - Verifica se
     * ao menos um registro foi excluído. - Exibe uma mensagem de sucesso e
     * chama o método `limparCons()` para limpar os campos. - Encerra a conexão
     * com o banco de dados.
     *
     * Tratamento de erro: - Captura exceções SQL e exibe mensagens de erro via
     * `JOptionPane`.
     *
     * Observações: - O método depende de variáveis externas como
     * `txtProdCodCon`, `conexao`, e do método `limparCons()`. - O nome do
     * método contém um erro de digitação ("excluiPodru" em vez de
     * "excluirProduto"). Recomenda-se renomear para refletir melhor sua função.
     */
    private void excluiPodru() {

        conexao = ModuloConexao.conector();

        String sql = "delete from cadastro_prod where cod =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodCon.getText());
            int apagado = pst.executeUpdate();
            if (apagado > 0) {
                JOptionPane.showMessageDialog(null, "Registro de Produto Excluído com Sucesso");
                limparCons();
                conexao.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método limpar.
     *
     * Restaura os campos da interface gráfica de cadastro de produto ao estado
     * inicial, removendo dados inseridos pelo usuário e redefinindo componentes
     * visuais.
     *
     * Funcionalidades: - Limpa os campos de texto relacionados ao nome,
     * fornecedor, nota fiscal e descrição do produto. - Reseta o valor do
     * spinner de quantidade para zero. - Define valores padrão para os combo
     * boxes de unidade de medida, setor e tipo. - Atualiza o componente de data
     * (`jdcProdDatValidade`) com a data atual no formato "dd/MM/yy". - Define a
     * imagem padrão no componente `lblProFoto`. - Reseta os dados da imagem
     * (`fis` e `tamanho`) para evitar reuso indevido. - Chama os métodos
     * `pesquisarProd()` e `ajustarTabela()` para atualizar a exibição da
     * tabela. - Desabilita os botões de adicionar e carregar imagem até que os
     * campos obrigatórios sejam preenchidos.
     *
     * Observações: - O método depende de vários componentes gráficos e
     * variáveis externas. - Pode ser aprimorado com validações para garantir
     * que os valores padrão estejam disponíveis.
     */
    private void limpar() {

        txaProdNome.setText("");
        txaProdFornecedor.setText("");
        txaProdNotFiscal.setText("");
        txaProdDescricao.setText("");
        spnProdQuantidade.setValue(0);
        cboProdUniMed.setSelectedItem("Quilograma(Kg)");
        cboProdSetor.setSelectedItem("A e B (Alimentos e Bebidas)");
        cboProdTipo.setSelectedItem("Alimentação");
        jdcProdDatValidade.setDateFormatString("dd/MM/yy");
        btnProdAdd.setEnabled(false);
        btnProdArquivoInicio.setEnabled(false);
        ZonedDateTime zdtNow = ZonedDateTime.now();
        jdcProdDatValidade.setDate(Timestamp.from(Instant.from(zdtNow)));
        ImageIcon icone = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/produto_sem_imagem.png"));
        Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblProFoto.getWidth(), lblProFoto.getHeight(), Image.SCALE_SMOOTH));
        lblProFoto.setIcon(foto);
        fis = null;
        tamanho = 0;
        pesquisarProd();
        ajustarTabela();
    }

    /**
     * Método limparCons.
     *
     * Restaura os campos da interface gráfica de consulta e modificação de
     * produtos ao estado inicial, removendo dados inseridos pelo usuário,
     * redefinindo componentes visuais e desabilitando botões e campos conforme
     * necessário.
     *
     * Funcionalidades: - Limpa os campos de texto relacionados a nome,
     * fornecedor, nota fiscal, descrição e quantidade. - Reseta os valores dos
     * spinners de adição e retirada para zero. - Limpa a tabela de notas
     * fiscais (`tblProdNotaFiscalCons`). - Define valores padrão para os combo
     * boxes de unidade de medida e setor. - Atualiza o componente de data com a
     * data atual no formato "dd/MM/yy". - Restaura a imagem padrão no
     * componente `lblProFoto`. - Reseta variáveis de imagem (`fis` e
     * `tamanho`). - Atualiza a tabela de produtos chamando `pesquisarProd()` e
     * `ajustarTabela()`. - Desabilita botões de ação e campos de edição
     * relacionados à consulta. - Reabilita os campos de cadastro para permitir
     * novo registro. - Define o modo de exibição como "Tipo" e configura os
     * botões de filtro.
     *
     * Observações: - O método depende de diversos componentes gráficos e
     * variáveis externas. - Pode ser dividido em métodos menores para melhorar
     * legibilidade e manutenção.
     */
    private void limparCons() {

        txaProdNomeCons.setText("");
        txaProdForCons.setText("");
        txaProdNotFiscal.setText("");
        txaProdDesCons.setText("");
        txtProdQuantCons.setText("");
        txaProdNotFiscalCons.setText("");
        txtProdPesquisar.setText(null);
        txtProdCodCon.setText(null);
        ((DefaultTableModel) tblProdNotaFiscalCons.getModel()).setRowCount(0);
        spnProdAdd.setValue(0);
        spnProdRetirar.setValue(0);
        cboProdUnidMedidaCons.setSelectedItem("Quilograma(Kg)");
        cboProdSetorCons.setSelectedItem("A e B (Alimentos e Bebidas)");
        ImageIcon icone = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/produto_sem_imagem.png"));
        Icon foto1 = new ImageIcon(icone.getImage().getScaledInstance(lblProFoto.getWidth(), lblProFoto.getHeight(), Image.SCALE_SMOOTH));
        lblProFoto.setIcon(foto1);
        fis = null;
        tamanho = 0;
        jdcProdDatValidadeCons.setDateFormatString("dd/MM/yy");
        ZonedDateTime zdtNow = ZonedDateTime.now();
        jdcProdDatValidadeCons.setDate(Timestamp.from(Instant.from(zdtNow)));
        pesquisarProd();

        btnProdAdd.setEnabled(false);
        btnProdAlte.setEnabled(false);
        btnProdApagar.setEnabled(false);
        btnProdNovo.setEnabled(false);
        btnProdArquivoInicio.setEnabled(false);
        btnProdModFoto.setEnabled(false);
        jdcProdDatValidade.setEnabled(true);
        txaProdNome.setEditable(true);
        txaProdFornecedor.setEditable(true);
        txaProdNotFiscal.setEditable(true);
        txaProdDescricao.setEditable(true);
        cboProdSetor.setEnabled(true);
        cboProdTipo.setEnabled(true);
        cboProdUniMed.setEnabled(true);
        spnProdQuantidade.setEnabled(true);

        txaProdNomeCons.setEditable(false);
        txaProdDesCons.setEditable(false);
        cboProdSetorCons.setEnabled(false);
        cboProdUnidMedidaCons.setEnabled(false);
        spnProdAdd.setEnabled(false);
        spnProdRetirar.setEnabled(false);
        combo = "Tipo";
        rbtProdTipoCons.setEnabled(false);
        rbtProdSetorCons.setEnabled(false);
        rbtProdTipoCons.setSelected(true);

        cboSelectCons();
        ajustarTabela();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cboProdTipo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaProdDescricao = new javax.swing.JTextArea();
        lblProFoto = new javax.swing.JLabel();
        cboProdSetor = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        txtProdCodCon = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProConsulta = new javax.swing.JTable();
        btnProdAdd = new javax.swing.JButton();
        btnProdAlte = new javax.swing.JButton();
        btnProdApagar = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnProdNovo = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnProdArquivoInicio = new javax.swing.JButton();
        btnProdModFoto = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtProdQuantCons = new javax.swing.JTextField();
        cboProdUniMed = new javax.swing.JComboBox<>();
        jdcProdDatValidade = new com.toedter.calendar.JDateChooser();
        spnProdQuantidade = new javax.swing.JSpinner();
        jScrollPane5 = new javax.swing.JScrollPane();
        txaProdNome = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        txaProdFornecedor = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        txaProdNotFiscal = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        spnProdAdd = new javax.swing.JSpinner();
        spnProdRetirar = new javax.swing.JSpinner();
        jLabel28 = new javax.swing.JLabel();
        txtProdPesquisar = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        aa = new javax.swing.JScrollPane();
        txaProdDesCons = new javax.swing.JTextArea();
        jdcProdDatValidadeCons = new com.toedter.calendar.JDateChooser();
        cboProdUnidMedidaCons = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaProdNomeCons = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaProdForCons = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        txaProdNotFiscalCons = new javax.swing.JTextArea();
        cboProdSetorPes = new javax.swing.JComboBox<>();
        cboProdSetorCons = new javax.swing.JComboBox<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblProdNotaFiscalCons = new javax.swing.JTable();
        rbtProdTipoCons = new javax.swing.JRadioButton();
        rbtProdSetorCons = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel2 = new JlabelGradient();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro e Atualização de Estoque");
        setOpaque(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 42)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Produtos e Materiais ");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(286, 20, 447, 60);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Descrição");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel4);
        jLabel4.setBounds(40, 382, 200, 24);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Tipo");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel6);
        jLabel6.setBounds(270, 186, 200, 24);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Setor");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel7);
        jLabel7.setBounds(270, 316, 200, 24);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Fornecedor");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel8);
        jLabel8.setBounds(40, 250, 220, 24);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Produto");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel9);
        jLabel9.setBounds(40, 186, 220, 24);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 51));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Quantidade");
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel10);
        jLabel10.setBounds(40, 316, 90, 24);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Nota Fiscal");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel11);
        jLabel11.setBounds(270, 250, 200, 24);

        cboProdTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alimentação", "Papelaria", "Manutenção", "Limpeza", "Outros" }));
        getContentPane().add(cboProdTipo);
        cboProdTipo.setBounds(270, 212, 200, 28);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Data de Validade");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel12);
        jLabel12.setBounds(40, 120, 120, 24);

        txaProdDescricao.setColumns(20);
        txaProdDescricao.setLineWrap(true);
        txaProdDescricao.setRows(5);
        txaProdDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdDescricaoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(txaProdDescricao);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(40, 408, 200, 40);

        lblProFoto.setBackground(new java.awt.Color(255, 255, 255));
        lblProFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lblProFoto.setOpaque(true);
        getContentPane().add(lblProFoto);
        lblProFoto.setBounds(34, 493, 120, 120);

        cboProdSetor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A e B (Alimentos e Bebidas)", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança" }));
        getContentPane().add(cboProdSetor);
        cboProdSetor.setBounds(270, 342, 200, 28);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Código");
        getContentPane().add(jLabel17);
        jLabel17.setBounds(528, 392, 90, 20);

        txtProdCodCon.setEditable(false);
        txtProdCodCon.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtProdCodCon);
        txtProdCodCon.setBounds(528, 412, 90, 28);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblProConsulta = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblProConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Produto", "Quantidade", "Imagem"
            }
        ));
        tblProConsulta.setRowHeight(48);
        tblProConsulta.getTableHeader().setResizingAllowed(false);
        tblProConsulta.getTableHeader().setReorderingAllowed(false);
        tblProConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProConsultaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblProConsulta);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(528, 140, 430, 160);

        btnProdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48761_file_add_file_upload_add_upload.png"))); // NOI18N
        btnProdAdd.setToolTipText("Adicionar Produto");
        btnProdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdAdd);
        btnProdAdd.setBounds(262, 488, 64, 64);

        btnProdAlte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Atualizar1.png"))); // NOI18N
        btnProdAlte.setToolTipText("Atualizar");
        btnProdAlte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdAlteActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdAlte);
        btnProdAlte.setBounds(338, 488, 64, 64);

        btnProdApagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48762_delete_delete file_delete_remove_delete_file_remove_remove_file.png"))); // NOI18N
        btnProdApagar.setToolTipText("Apagar");
        btnProdApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdApagarActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdApagar);
        btnProdApagar.setBounds(414, 488, 64, 64);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 51));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Medida");
        getContentPane().add(jLabel23);
        jLabel23.setBounds(628, 508, 120, 20);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Quantidade");
        getContentPane().add(jLabel22);
        jLabel22.setBounds(528, 508, 90, 20);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 51));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Medida");
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel20);
        jLabel20.setBounds(140, 316, 120, 24);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 51));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Produto");
        getContentPane().add(jLabel18);
        jLabel18.setBounds(759, 392, 200, 20);

        btnProdNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48760_file_file.png"))); // NOI18N
        btnProdNovo.setToolTipText("Nova Consulta");
        btnProdNovo.setBorderPainted(false);
        btnProdNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdNovoActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdNovo);
        btnProdNovo.setBounds(262, 555, 64, 64);

        jLabel13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        getContentPane().add(jLabel13);
        jLabel13.setBounds(250, 484, 240, 146);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Fornecedor");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(528, 448, 220, 20);

        btnProdArquivoInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48769_folder_empty_folder_empty.png"))); // NOI18N
        btnProdArquivoInicio.setToolTipText("Selecionar Imagem");
        btnProdArquivoInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdArquivoInicioActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdArquivoInicio);
        btnProdArquivoInicio.setBounds(164, 488, 64, 64);

        btnProdModFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Atualizar1.png"))); // NOI18N
        btnProdModFoto.setToolTipText("Atualizar Imagem");
        btnProdModFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdModFotoActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdModFoto);
        btnProdModFoto.setBounds(164, 555, 64, 64);

        jLabel24.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Imagem", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 0, 51))); // NOI18N
        getContentPane().add(jLabel24);
        jLabel24.setBounds(20, 470, 220, 160);

        txtProdQuantCons.setEditable(false);
        txtProdQuantCons.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProdQuantCons.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtProdQuantCons);
        txtProdQuantCons.setBounds(528, 530, 90, 28);

        cboProdUniMed.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cboProdUniMed.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quilograma(Kg)", "Caixa(Cx)", "Metros(M)", "Litros(L)", "Unidades(U)", "Outros" }));
        getContentPane().add(cboProdUniMed);
        cboProdUniMed.setBounds(140, 342, 120, 28);

        jdcProdDatValidade.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        getContentPane().add(jdcProdDatValidade);
        jdcProdDatValidade.setBounds(40, 146, 120, 28);

        spnProdQuantidade.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9999999, 1));
        getContentPane().add(spnProdQuantidade);
        spnProdQuantidade.setBounds(40, 342, 90, 28);

        txaProdNome.setColumns(20);
        txaProdNome.setLineWrap(true);
        txaProdNome.setRows(5);
        txaProdNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txaProdNomeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdNomeKeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(txaProdNome);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(40, 212, 220, 36);

        txaProdFornecedor.setColumns(20);
        txaProdFornecedor.setLineWrap(true);
        txaProdFornecedor.setRows(5);
        txaProdFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txaProdFornecedorKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdFornecedorKeyTyped(evt);
            }
        });
        jScrollPane6.setViewportView(txaProdFornecedor);

        getContentPane().add(jScrollPane6);
        jScrollPane6.setBounds(40, 276, 220, 36);

        txaProdNotFiscal.setColumns(20);
        txaProdNotFiscal.setLineWrap(true);
        txaProdNotFiscal.setRows(5);
        txaProdNotFiscal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txaProdNotFiscalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdNotFiscalKeyTyped(evt);
            }
        });
        jScrollPane7.setViewportView(txaProdNotFiscal);

        getContentPane().add(jScrollPane7);
        jScrollPane7.setBounds(270, 276, 200, 36);

        jLabel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(0, 0, 51))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 90, 470, 380);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 51));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Nota Fiscal");
        getContentPane().add(jLabel19);
        jLabel19.setBounds(759, 448, 200, 20);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 51));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Adicionar");
        getContentPane().add(jLabel21);
        jLabel21.setBounds(526, 562, 90, 20);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 51));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Retirar");
        getContentPane().add(jLabel25);
        jLabel25.setBounds(628, 562, 90, 20);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 51));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Data de Validade");
        getContentPane().add(jLabel26);
        jLabel26.setBounds(628, 392, 120, 20);

        spnProdAdd.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9999999, 1));
        spnProdAdd.setEditor(new javax.swing.JSpinner.NumberEditor(spnProdAdd, ""));
        spnProdAdd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnProdAddStateChanged(evt);
            }
        });
        getContentPane().add(spnProdAdd);
        spnProdAdd.setBounds(528, 582, 90, 28);

        spnProdRetirar.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9999999, 1));
        spnProdRetirar.setEditor(new javax.swing.JSpinner.NumberEditor(spnProdRetirar, ""));
        getContentPane().add(spnProdRetirar);
        spnProdRetirar.setBounds(628, 582, 90, 28);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Descrição");
        getContentPane().add(jLabel28);
        jLabel28.setBounds(759, 562, 200, 20);

        txtProdPesquisar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtProdPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProdPesquisarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtProdPesquisarKeyTyped(evt);
            }
        });
        getContentPane().add(txtProdPesquisar);
        txtProdPesquisar.setBounds(627, 110, 150, 28);

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 51));
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        jLabel30.setText("Pesquisar");
        getContentPane().add(jLabel30);
        jLabel30.setBounds(534, 110, 92, 28);

        txaProdDesCons.setColumns(20);
        txaProdDesCons.setLineWrap(true);
        txaProdDesCons.setRows(5);
        txaProdDesCons.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdDesConsKeyTyped(evt);
            }
        });
        aa.setViewportView(txaProdDesCons);

        getContentPane().add(aa);
        aa.setBounds(759, 582, 200, 40);

        jdcProdDatValidadeCons.setEnabled(false);
        getContentPane().add(jdcProdDatValidadeCons);
        jdcProdDatValidadeCons.setBounds(628, 412, 120, 28);

        cboProdUnidMedidaCons.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cboProdUnidMedidaCons.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quilograma(Kg)", "Caixa(Cx)", "Metros(M)", "Litros(L)", "Unidades(U)", "Outros" }));
        getContentPane().add(cboProdUnidMedidaCons);
        cboProdUnidMedidaCons.setBounds(628, 530, 120, 28);

        txaProdNomeCons.setEditable(false);
        txaProdNomeCons.setColumns(20);
        txaProdNomeCons.setLineWrap(true);
        txaProdNomeCons.setRows(5);
        txaProdNomeCons.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdNomeConsKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(txaProdNomeCons);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(759, 412, 200, 36);

        txaProdForCons.setEditable(false);
        txaProdForCons.setColumns(20);
        txaProdForCons.setLineWrap(true);
        txaProdForCons.setRows(5);
        txaProdForCons.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdForConsKeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(txaProdForCons);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(528, 468, 220, 36);

        txaProdNotFiscalCons.setEditable(false);
        txaProdNotFiscalCons.setColumns(20);
        txaProdNotFiscalCons.setLineWrap(true);
        txaProdNotFiscalCons.setRows(5);
        txaProdNotFiscalCons.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaProdNotFiscalConsKeyTyped(evt);
            }
        });
        jScrollPane8.setViewportView(txaProdNotFiscalCons);

        getContentPane().add(jScrollPane8);
        jScrollPane8.setBounds(759, 468, 200, 36);

        cboProdSetorPes.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        cboProdSetorPes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "A e B (Alimentos e Bebidas)", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança" }));
        cboProdSetorPes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdSetorPesActionPerformed(evt);
            }
        });
        getContentPane().add(cboProdSetorPes);
        cboProdSetorPes.setBounds(781, 110, 176, 28);

        cboProdSetorCons.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A e B (Alimentos e Bebidas)", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança" }));
        cboProdSetorCons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdSetorConsActionPerformed(evt);
            }
        });
        getContentPane().add(cboProdSetorCons);
        cboProdSetorCons.setBounds(759, 530, 200, 28);

        jScrollPane10.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblProdNotaFiscalCons = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblProdNotaFiscalCons.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nota Fiscal", "Validade"
            }
        ));
        tblProdNotaFiscalCons.getTableHeader().setResizingAllowed(false);
        tblProdNotaFiscalCons.getTableHeader().setReorderingAllowed(false);
        tblProdNotaFiscalCons.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdNotaFiscalConsMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblProdNotaFiscalCons);

        getContentPane().add(jScrollPane10);
        jScrollPane10.setBounds(528, 300, 430, 90);

        buttonGroup1.add(rbtProdTipoCons);
        rbtProdTipoCons.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbtProdTipoCons.setForeground(new java.awt.Color(0, 0, 51));
        rbtProdTipoCons.setText("Tipo");
        rbtProdTipoCons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtProdTipoConsActionPerformed(evt);
            }
        });
        getContentPane().add(rbtProdTipoCons);
        rbtProdTipoCons.setBounds(760, 508, 90, 20);

        buttonGroup1.add(rbtProdSetorCons);
        rbtProdSetorCons.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbtProdSetorCons.setForeground(new java.awt.Color(0, 0, 51));
        rbtProdSetorCons.setText("Setor");
        rbtProdSetorCons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtProdSetorConsActionPerformed(evt);
            }
        });
        getContentPane().add(rbtProdSetorCons);
        rbtProdSetorCons.setBounds(868, 508, 88, 20);

        jLabel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consultar/Atualizar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(0, 0, 51))); // NOI18N
        getContentPane().add(jLabel14);
        jLabel14.setBounds(508, 90, 470, 540);

        jLabel2.setForeground(new java.awt.Color(0, 0, 120));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(-10, 0, 1010, 640);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método formInternalFrameOpened.
     *
     * Executado automaticamente quando o `JInternalFrame` é aberto. Este método
     * inicializa diversos componentes da interface gráfica relacionados à
     * consulta e cadastro de produtos.
     *
     * Funcionalidades: - Define o valor inicial da variável `pesquisa` com base
     * na seleção atual do combo box `cboProdSetorPes`. - Limpa a tabela de
     * notas fiscais (`tblProdNotaFiscalCons`) e atualiza os dados com
     * `pesquisarProd()`. - Carrega e exibe a imagem padrão no componente
     * `lblProFoto`. - Define o modo de exibição como "Tipo" e configura o combo
     * box de setor. - Configura os `JSpinner` para aceitar apenas valores
     * numéricos válidos. - Define o formato de data para os componentes de data
     * (`jdcProdDatValidade` e `jdcProdDatValidadeCons`) e os inicializa com a
     * data atual. - Define ícones personalizados para os componentes de data. -
     * Desabilita botões de ação e campos de edição relacionados à consulta. -
     * Configura os botões de filtro e seus estilos visuais.
     *
     * Observações: - Este método é disparado por um `InternalFrameEvent`,
     * geralmente associado ao evento `internalFrameOpened`. - Ideal para
     * garantir que a interface esteja pronta e limpa ao ser exibida ao usuário.
     *
     * @param evt Evento que representa a abertura do `JInternalFrame`.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        pesquisa = cboProdSetorPes.getSelectedItem().toString();
        ((DefaultTableModel) tblProdNotaFiscalCons.getModel()).setRowCount(0);
        pesquisarProd();
        ImageIcon icone = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/produto_sem_imagem.png"));
        Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblProFoto.getWidth(), lblProFoto.getHeight(), Image.SCALE_SMOOTH));
        lblProFoto.setIcon(foto);

        combo = "Tipo";
        cboSelectCons();
        cboProdSetorCons.setSelectedItem("A e B (Alimentos e Bebidas)");

        ((DefaultFormatter) ((JSpinner.NumberEditor) spnProdAdd.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false); //impede que se digite qualquer caracter não-numérico no spinner
        ((DefaultFormatter) ((JSpinner.NumberEditor) spnProdRetirar.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false); //impede que se digite qualquer caracter não-numérico no spinner
        ((DefaultFormatter) ((JSpinner.NumberEditor) spnProdQuantidade.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false); //impede que se digite qualquer caracter não-numérico no spinner

        jdcProdDatValidade.setDateFormatString("dd/MM/yy");
        jdcProdDatValidadeCons.setDateFormatString("dd/MM/yy");
        ZonedDateTime zdtNow = ZonedDateTime.now();
        jdcProdDatValidade.setDate(Timestamp.from(Instant.from(zdtNow)));
        jdcProdDatValidadeCons.setDate(Timestamp.from(Instant.from(zdtNow)));
        ImageIcon icon = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/data.png"));
        jdcProdDatValidade.setIcon(icon);
        jdcProdDatValidadeCons.setIcon(icon);

        btnProdAdd.setEnabled(false);
        btnProdArquivoInicio.setEnabled(false);
        btnProdModFoto.setEnabled(false);
        btnProdAlte.setEnabled(false);
        btnProdApagar.setEnabled(false);
        btnProdNovo.setEnabled(false);

        txaProdDesCons.setEditable(false);
        cboProdSetorCons.setEnabled(false);
        cboProdUnidMedidaCons.setEnabled(false);
        spnProdAdd.setEnabled(false);
        spnProdRetirar.setEnabled(false);
        rbtProdTipoCons.setSelected(true);
        rbtProdTipoCons.setForeground(new Color(39, 44, 182));
        rbtProdTipoCons.setEnabled(false);
        rbtProdSetorCons.setEnabled(false);
    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método txaProdNomeKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdNome`,
     * permitindo apenas letras (maiúsculas e minúsculas), acentuações comuns,
     * números e alguns símbolos específicos. Caso o caractere digitado não
     * esteja na lista permitida, o evento é consumido, impedindo que o
     * caractere apareça no campo.
     *
     * Caracteres permitidos: Letras: A-Z (com variações acentuadas como Á, Ã,
     * Ç, etc.) Números: 0-9 Símbolos: - + _ ( ) / , ; . & @ e espaço
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdNomeKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdNomeKeyTyped

    /**
     * Método txaProdNotFiscalKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdNotFiscal`,
     * permitindo apenas números e o ponto decimal. Caso o caractere digitado
     * não esteja na lista permitida, o evento é consumido, impedindo que o
     * caractere apareça no campo.
     *
     * Caracteres permitidos: - Dígitos: 0–9 - Ponto: .
     *
     * Utilizado para garantir que o campo receba apenas valores válidos para
     * notas fiscais, como números decimais.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdNotFiscalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdNotFiscalKeyTyped

        String caracter = "0123456789.";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdNotFiscalKeyTyped

    /**
     * Método txaProdFornecedorKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdFornecedor`,
     * permitindo apenas letras (com acentuação), números e símbolos
     * específicos. Caso o caractere digitado não esteja na lista permitida, o
     * evento é consumido, impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Letras: A–Z (maiúsculas e minúsculas), incluindo
     * acentos como Á, Ã, Ç, etc. - Números: 0–9 - Símbolos: - + _ ( ) / , ; . &
     *
     * @ e espaço
     *
     * Utilizado para garantir que o nome do fornecedor seja inserido com
     * formatação válida.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdFornecedorKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdFornecedorKeyTyped

    /**
     * Método txaProdDescricaoKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdDescricao`,
     * permitindo apenas letras (com acentuação), números e símbolos
     * específicos. Caso o caractere digitado não esteja na lista permitida, o
     * evento é consumido, impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Letras: A–Z (maiúsculas e minúsculas), incluindo
     * acentos como Á, Ã, Ç, etc. - Números: 0–9 - Símbolos: - + _ ( ) / , ; . &
     *
     * @ e espaço
     *
     * Utilizado para garantir que a descrição do produto seja inserida com
     * formatação válida.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdDescricaoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdDescricaoKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdDescricaoKeyTyped

    /**
     * Método txaProdNomeConsKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdNomeCons`,
     * permitindo apenas letras (com acentuação), números e símbolos
     * específicos. Caso o caractere digitado não esteja na lista permitida, o
     * evento é consumido, impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Letras: A–Z (maiúsculas e minúsculas), incluindo
     * acentos como Á, Ã, Ç, etc. - Números: 0–9 - Símbolos: - + _ ( ) / , ; . &
     *
     * @ e espaço
     *
     * Utilizado para garantir que o nome do produto na consulta seja inserido
     * com formatação válida.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdNomeConsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdNomeConsKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdNomeConsKeyTyped

    /**
     * Método txaProdForConsKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdForCons`,
     * permitindo apenas letras (com acentuação), números e símbolos
     * específicos. Caso o caractere digitado não esteja na lista permitida, o
     * evento é consumido, impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Letras: A–Z (maiúsculas e minúsculas), incluindo
     * acentos como Á, Ã, Ç, etc. - Números: 0–9 - Símbolos: - + _ ( ) / , ; . &
     *
     * @ e espaço
     *
     * Utilizado para garantir que o nome do fornecedor na consulta seja
     * inserido com formatação válida.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdForConsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdForConsKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdForConsKeyTyped

    /**
     * Método txaProdNotFiscalConsKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto
     * `txaProdNotFiscalCons`, permitindo apenas dígitos numéricos. Caso o
     * caractere digitado não esteja na lista permitida, o evento é consumido,
     * impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Dígitos: 0–9
     *
     * Utilizado para garantir que o campo de nota fiscal na área de consulta
     * receba apenas números válidos.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdNotFiscalConsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdNotFiscalConsKeyTyped

        String caracter = "0123456789";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdNotFiscalConsKeyTyped

    /**
     * Método txaProdDesConsKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdForCons`,
     * permitindo apenas letras (com acentuação), números e símbolos
     * específicos. Caso o caractere digitado não esteja na lista permitida, o
     * evento é consumido, impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Letras: A–Z (maiúsculas e minúsculas), incluindo
     * acentos como Á, Ã, Ç, etc. - Números: 0–9 - Símbolos: - + _ ( ) / , ; . &
     *
     * @ e espaço
     *
     * Utilizado para garantir que o nome do fornecedor na consulta seja
     * inserido com formatação válida.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txaProdDesConsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdDesConsKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txaProdDesConsKeyTyped

    /**
     * Método txtProdPesquisarKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdForCons`,
     * permitindo apenas letras (com acentuação), números e símbolos
     * específicos. Caso o caractere digitado não esteja na lista permitida, o
     * evento é consumido, impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Letras: A–Z (maiúsculas e minúsculas), incluindo
     * acentos como Á, Ã, Ç, etc. - Números: 0–9 - Símbolos: - + _ ( ) / , ; . &
     *
     * @ e espaço
     *
     * Utilizado para garantir que o nome do fornecedor na consulta seja
     * inserido com formatação válida.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txtProdPesquisarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProdPesquisarKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtProdPesquisarKeyTyped

    /**
     * Método btnProdArquivoInicioActionPerformed.
     *
     * Executado quando o botão `btnProdArquivoInicio` é clicado. Define o valor
     * da variável `str` como `"cadastro"` e chama o método `carregarImag()`
     * para iniciar o processo de carregamento de imagem, possivelmente
     * associada ao cadastro de produto.
     *
     * Utilização típica: - Preparar o sistema para carregar uma imagem
     * relacionada ao processo de cadastro. - Pode ser parte de uma interface
     * gráfica Swing com botões que disparam ações via `ActionListener`.
     *
     * @param evt Evento de ação gerado pelo clique no botão.
     */
    private void btnProdArquivoInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdArquivoInicioActionPerformed

        str = "cadastro";
        carregarImag();
    }//GEN-LAST:event_btnProdArquivoInicioActionPerformed

    /**
     * Método btnProdAddActionPerformed.
     *
     * Executado quando o botão `btnProdAdd` é clicado. Este método chama
     * `conferirImagem()`, que provavelmente realiza a verificação ou validação
     * da imagem associada ao produto antes de prosseguir com o processo de
     * adição.
     *
     * Utilização típica: - Validar se uma imagem foi carregada corretamente. -
     * Preparar os dados visuais antes de salvar ou adicionar um novo produto.
     *
     * @param evt Evento de ação gerado pelo clique no botão.
     */
    private void btnProdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdAddActionPerformed

        conferirImagem();
    }//GEN-LAST:event_btnProdAddActionPerformed

    /**
     * Método btnProdAlteActionPerformed.
     *
     * Executado quando o botão `btnProdAlte` é clicado. Este método chama
     * `produtoCalculo()`, que provavelmente realiza cálculos relacionados ao
     * produto, como atualização de quantidade, preço, validade ou outros
     * atributos antes de aplicar alterações no cadastro.
     *
     * Utilização típica: - Processar dados do produto antes de salvar
     * alterações. - Validar ou recalcular informações com base em entradas do
     * usuário.
     *
     * @param evt Evento de ação gerado pelo clique no botão.
     */
    private void btnProdAlteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdAlteActionPerformed

        produtoCalculo();
    }//GEN-LAST:event_btnProdAlteActionPerformed

    /**
     * Método cboProdSetorPesActionPerformed.
     *
     * Executado quando o usuário seleciona uma nova opção no combo box
     * `cboProdSetorPes`. Atualiza a variável `pesquisa` com o valor
     * selecionado, realiza nova busca de produtos com base nesse setor, ajusta
     * a tabela de exibição e limpa os campos da interface de consulta.
     *
     * Funcionalidades: - Atualiza o filtro de pesquisa com base no setor
     * selecionado. - Chama `pesquisarProd()` para atualizar os dados exibidos.
     * - Chama `ajustarTabela()` para reorganizar a visualização da tabela. -
     * Limpa o campo de pesquisa (`txtProdPesquisar`) e os campos de consulta
     * com `limparCons()`.
     *
     * @param evt Evento de ação gerado pela alteração na seleção do combo box.
     */
    private void cboProdSetorPesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdSetorPesActionPerformed

        pesquisa = cboProdSetorPes.getSelectedItem().toString();
        pesquisarProd();
        ajustarTabela();
        txtProdPesquisar.setText("");
        limparCons();
    }//GEN-LAST:event_cboProdSetorPesActionPerformed

    /**
     * Método txtProdPesquisarKeyReleased.
     *
     * Executado sempre que uma tecla é liberada no campo de texto
     * `txtProdPesquisar`. Este método atualiza dinamicamente os dados exibidos
     * na interface com base no conteúdo atual do campo de pesquisa.
     *
     * Funcionalidades: - Chama `pesquisarProd()` para realizar uma nova busca
     * de produtos com base no texto digitado. - Chama `ajustarTabela()` para
     * reorganizar a visualização da tabela conforme os resultados da pesquisa.
     *
     * Utilização típica: - Implementar busca em tempo real conforme o usuário
     * digita. - Melhorar a experiência do usuário com feedback imediato.
     *
     * @param evt Evento de tecla gerado quando o usuário libera uma tecla no
     * campo de pesquisa.
     */
    private void txtProdPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProdPesquisarKeyReleased

        pesquisarProd();
        ajustarTabela();
    }//GEN-LAST:event_txtProdPesquisarKeyReleased

    /**
     * Método tblProConsultaMouseClicked.
     *
     * Executado quando o usuário clica em uma linha da tabela `tblProConsulta`.
     * Este método prepara a interface para exibir os dados do produto
     * selecionado e habilita funcionalidades relacionadas à modificação ou
     * visualização.
     *
     * Funcionalidades: - Define o modo de exibição como "Tipo". - Chama
     * `servicosCon()` para carregar os dados do produto selecionado. - Reseta
     * os spinners de adição e retirada para zero e desabilita o spinner de
     * retirada. - Chama `setProd()` para configurar os dados do produto na
     * interface. - Chama `limpar()` para limpar campos auxiliares. - Atualiza o
     * estado visual dos botões de filtro (`rbtProdTipoCons`,
     * `rbtProdSetorCons`) e os habilita. - Define o setor modificado
     * (`setorMod`) com base no setor atual. - Atualiza o combo box
     * `cboProdSetorCons` com o tipo selecionado. - Habilita o botão
     * `btnProdArquivoInicio` para permitir carregamento de imagem.
     *
     * @param evt Evento de clique do mouse gerado ao interagir com a tabela.
     */
    private void tblProConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProConsultaMouseClicked

        combo = "Tipo";
        servicosCon();
        spnProdAdd.setValue(0);
        spnProdRetirar.setValue(0);
        spnProdRetirar.setEnabled(false);
        setProd();
        limpar();

        rbtProdTipoCons.setSelected(true);
        setorMod = setor;
        cboProdSetorCons.setSelectedItem(tipo);
        btnProdArquivoInicio.setEnabled(true);
        rbtProdTipoCons.setEnabled(true);
        rbtProdSetorCons.setEnabled(true);
        rbtProdTipoCons.setForeground(new Color(39, 44, 182));
        rbtProdSetorCons.setForeground(new Color(0, 0, 51));
    }//GEN-LAST:event_tblProConsultaMouseClicked

    /**
     * Método spnProdAddStateChanged.
     *
     * Executado quando o valor do componente `JSpinner` chamado `spnProdAdd` é
     * alterado. Este método chama `comparar()`, que provavelmente realiza uma
     * verificação ou cálculo com base no novo valor do spinner, como comparação
     * com estoque disponível ou limites definidos.
     *
     * Utilização típica: - Validar ou ajustar dados em tempo real conforme o
     * usuário altera a quantidade a adicionar. - Atualizar a interface ou
     * lógica de negócios com base na nova entrada.
     *
     * @param evt Evento de alteração gerado pelo componente `JSpinner`.
     */
    private void spnProdAddStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnProdAddStateChanged

        comparar();
    }//GEN-LAST:event_spnProdAddStateChanged

    /**
     * Método btnProdModFotoActionPerformed.
     *
     * Executado quando o botão `btnProdModFoto` é clicado. Este método realiza
     * a atualização da imagem associada ao produto por meio da chamada ao
     * método `atualizarImagem()`, e em seguida atualiza a lista de produtos
     * exibida na interface com `pesquisarProd()`.
     *
     * Funcionalidades: - Substitui ou modifica a imagem atual do produto. -
     * Recarrega os dados da interface para refletir a alteração.
     *
     * Utilização típica: - Atualizar visualmente a imagem de um produto
     * cadastrado. - Garantir que a interface exiba os dados mais recentes após
     * a modificação.
     *
     * @param evt Evento de ação gerado pelo clique no botão.
     */
    private void btnProdModFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdModFotoActionPerformed

        atualizarImagem();
        pesquisarProd();
    }//GEN-LAST:event_btnProdModFotoActionPerformed

    /**
     * Método txaProdNomeKeyReleased.
     *
     * Executado quando uma tecla é liberada no campo de texto `txaProdNome`.
     * Chama o método `verificacao()` para realizar validações ou atualizações
     * com base no conteúdo atual do campo.
     *
     * Utilização típica: - Validar se o nome do produto está preenchido
     * corretamente. - Atualizar dinamicamente o estado da interface ou
     * habilitar botões.
     *
     * @param evt Evento de tecla gerado ao liberar uma tecla.
     */
    private void txaProdNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdNomeKeyReleased

        verificacao();
    }//GEN-LAST:event_txaProdNomeKeyReleased

    /**
     * Método txaProdFornecedorKeyReleased.
     *
     * Executado quando uma tecla é liberada no campo de texto
     * `txaProdFornecedor`. Chama o método `verificacao()` para realizar
     * validações ou atualizações com base no conteúdo atual do campo.
     *
     * Utilização típica: - Validar se o nome do fornecedor está preenchido
     * corretamente. - Atualizar dinamicamente o estado da interface ou
     * habilitar botões.
     *
     * @param evt Evento de tecla gerado ao liberar uma tecla.
     */
    private void txaProdFornecedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdFornecedorKeyReleased

        verificacao();
    }//GEN-LAST:event_txaProdFornecedorKeyReleased

    /**
     * Método txaProdNotFiscalKeyReleased.
     *
     * Executado quando uma tecla é liberada no campo de texto
     * `txaProdNotFiscal`. Chama o método `verificacao()` para realizar
     * validações ou atualizações com base no conteúdo atual do campo.
     *
     * Utilização típica: - Validar se o número da nota fiscal está preenchido
     * corretamente. - Atualizar dinamicamente o estado da interface ou
     * habilitar botões.
     *
     * @param evt Evento de tecla gerado ao liberar uma tecla.
     */
    private void txaProdNotFiscalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaProdNotFiscalKeyReleased

        verificacao();
    }//GEN-LAST:event_txaProdNotFiscalKeyReleased

    /**
     * Método tblProdNotaFiscalConsMouseClicked.
     *
     * Executado quando o usuário clica em uma linha da tabela
     * `tblProdNotaFiscalCons`. Este método prepara a interface para exibir os
     * dados da nota fiscal selecionada e configura os filtros e campos
     * relacionados.
     *
     * Funcionalidades: - Define o modo de exibição como "Tipo". - Chama
     * `setNotaFiscal()` para carregar os dados da nota fiscal selecionada. -
     * Atualiza os filtros com `cboSelectCons()`. - Seleciona o botão de filtro
     * por tipo (`rbtProdTipoCons`) e atualiza seu estilo visual. - Atualiza o
     * combo box `cboProdSetorCons` com o tipo selecionado. - Define `setorMod`
     * com base no setor atual. - Desabilita os botões de filtro para impedir
     * alterações imediatas.
     *
     * @param evt Evento de clique do mouse gerado ao interagir com a tabela.
     */
    private void tblProdNotaFiscalConsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdNotaFiscalConsMouseClicked

        combo = "Tipo";
        setNotaFiscal();
        cboSelectCons();

        rbtProdTipoCons.setSelected(true);
        cboProdSetorCons.setSelectedItem(tipo);
        setorMod = setor;
        rbtProdTipoCons.setEnabled(false);
        rbtProdSetorCons.setEnabled(false);

    }//GEN-LAST:event_tblProdNotaFiscalConsMouseClicked

    /**
     * Método btnProdNovoActionPerformed.
     *
     * Executado quando o botão `btnProdNovo` é clicado. Este método redefine o
     * filtro de setor para "Todos" e limpa os campos da interface de consulta
     * de produtos.
     *
     * Funcionalidades: - Atualiza o combo box `cboProdSetorPes` para "Todos". -
     * Chama `limparCons()` para restaurar os campos da interface ao estado
     * inicial.
     *
     * @param evt Evento de ação gerado pelo clique no botão.
     */
    private void btnProdNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdNovoActionPerformed

        cboProdSetorPes.setSelectedItem("Todos");
        limparCons();
    }//GEN-LAST:event_btnProdNovoActionPerformed

    /**
     * Método btnProdApagarActionPerformed.
     *
     * Executado quando o botão `btnProdApagar` é clicado. Exibe uma caixa de
     * diálogo de confirmação para o usuário. Se o usuário confirmar a exclusão,
     * chama o método `verificarQuantidadeExcluir()` para realizar a remoção do
     * produto.
     *
     * Funcionalidades: - Exibe `JOptionPane` com opções "Sim" e "Não". -
     * Executa a exclusão apenas se o usuário confirmar.
     *
     * @param evt Evento de ação gerado pelo clique no botão.
     */
    private void btnProdApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdApagarActionPerformed

        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que Deseja Remover este Produto?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            verificarQuantidadeExcluir();
        }
    }//GEN-LAST:event_btnProdApagarActionPerformed

    /**
     * Método rbtProdTipoConsActionPerformed.
     *
     * Executado quando o botão de rádio `rbtProdTipoCons` é selecionado. Este
     * método atualiza a interface para refletir o filtro por tipo de produto.
     *
     * Funcionalidades: - Define a cor do botão `rbtProdTipoCons` como azul e do
     * `rbtProdSetorCons` como cinza escuro. - Define o modo de exibição como
     * "Tipo". - Atualiza os dados exibidos com `cboSelectCons()`. - Atualiza o
     * combo box `cboProdSetorCons` com o tipo atual.
     *
     * @param evt Evento de ação gerado pela seleção do botão de rádio.
     */
    private void rbtProdTipoConsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtProdTipoConsActionPerformed

        rbtProdTipoCons.setForeground(new Color(39, 44, 182));
        rbtProdSetorCons.setForeground(new Color(0, 0, 51));
        combo = "Tipo";
        cboSelectCons();
        cboProdSetorCons.setSelectedItem(tipo);
    }//GEN-LAST:event_rbtProdTipoConsActionPerformed

    /**
     * Método rbtProdSetorConsActionPerformed.
     *
     * Executado quando o botão de rádio `rbtProdSetorCons` é selecionado. Este
     * método atualiza a interface para refletir o filtro por setor de produto.
     *
     * Funcionalidades: - Define a cor do botão `rbtProdSetorCons` como azul e
     * do `rbtProdTipoCons` como cinza escuro. - Define o modo de exibição como
     * "Setor". - Atualiza os dados exibidos com `cboSelectCons()`. - Atualiza o
     * combo box `cboProdSetorCons` com o setor atual.
     *
     * @param evt Evento de ação gerado pela seleção do botão de rádio.
     */
    private void rbtProdSetorConsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtProdSetorConsActionPerformed

        rbtProdTipoCons.setForeground(new Color(0, 0, 51));
        rbtProdSetorCons.setForeground(new Color(39, 44, 182));
        combo = "Setor";
        cboSelectCons();
        cboProdSetorCons.setSelectedItem(setor);
    }//GEN-LAST:event_rbtProdSetorConsActionPerformed

    /**
     * Método cboProdSetorConsActionPerformed.
     *
     * Executado quando o usuário seleciona uma nova opção no combo box
     * `cboProdSetorCons`. Este método atualiza a variável `tipoMod` ou
     * `setorMod` dependendo do modo de exibição atual definido pela variável
     * `combo`.
     *
     * Funcionalidades: - Se `combo` for "Tipo", atualiza `tipoMod` com o valor
     * selecionado. - Caso contrário, atualiza `setorMod` com o valor
     * selecionado.
     *
     * @param evt Evento de ação gerado pela alteração na seleção do combo box.
     */
    private void cboProdSetorConsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdSetorConsActionPerformed

        if (combo.equals("Tipo")) {
            tipoMod = cboProdSetorCons.getSelectedItem().toString();
        } else {
            setorMod = cboProdSetorCons.getSelectedItem().toString();
        }
    }//GEN-LAST:event_cboProdSetorConsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane aa;
    private javax.swing.JButton btnProdAdd;
    private javax.swing.JButton btnProdAlte;
    private javax.swing.JButton btnProdApagar;
    private javax.swing.JButton btnProdArquivoInicio;
    private javax.swing.JButton btnProdModFoto;
    private javax.swing.JButton btnProdNovo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboProdSetor;
    private javax.swing.JComboBox<String> cboProdSetorCons;
    private javax.swing.JComboBox<String> cboProdSetorPes;
    private javax.swing.JComboBox<String> cboProdTipo;
    private javax.swing.JComboBox<String> cboProdUniMed;
    private javax.swing.JComboBox<String> cboProdUnidMedidaCons;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private com.toedter.calendar.JDateChooser jdcProdDatValidade;
    private com.toedter.calendar.JDateChooser jdcProdDatValidadeCons;
    private javax.swing.JLabel lblProFoto;
    private javax.swing.JRadioButton rbtProdSetorCons;
    private javax.swing.JRadioButton rbtProdTipoCons;
    private javax.swing.JSpinner spnProdAdd;
    private javax.swing.JSpinner spnProdQuantidade;
    private javax.swing.JSpinner spnProdRetirar;
    private javax.swing.JTable tblProConsulta;
    private javax.swing.JTable tblProdNotaFiscalCons;
    private javax.swing.JTextArea txaProdDesCons;
    private javax.swing.JTextArea txaProdDescricao;
    private javax.swing.JTextArea txaProdForCons;
    private javax.swing.JTextArea txaProdFornecedor;
    private javax.swing.JTextArea txaProdNome;
    private javax.swing.JTextArea txaProdNomeCons;
    private javax.swing.JTextArea txaProdNotFiscal;
    private javax.swing.JTextArea txaProdNotFiscalCons;
    private javax.swing.JTextField txtProdCodCon;
    private javax.swing.JTextField txtProdPesquisar;
    private javax.swing.JTextField txtProdQuantCons;
    // End of variables declaration//GEN-END:variables
}
