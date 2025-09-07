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

import java.sql.*;
import com.prjmanutencao.dal.ModuloConexao;
import javax.swing.JOptionPane;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import net.proteanit.sql.DbUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Realiza o cadastro, atualização e a exclusão de usários.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaUsuarios extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String des = null;
    public static String ent2 = null;
    String ent3 = null;
    String nom_arq1 = null;
    String dir = null;
    String fe = "";
    String Imag = null;
    String pesq = null;
    String outros;
    public static String acesso = ("127.0.0.1");
    int porta = 21;
    String log = "servidor";
    String sen = "12345";
    String dire = "fotos_usu";
    String data = null;

    /**
     * Construtor da classe `TelaUsuarios`. Responsável por inicializar os
     * componentes da interface, realizar configurações iniciais, como alinhar
     * dados, definir valores padrão para os campos, configurar conexões com o
     * banco de dados e preparar a interface para uso.
     */
    public TelaUsuarios() {
        initComponents();
        contagem();
        alinhar();

        conexao = ModuloConexao.conector();
        cboUsuPer.setSelectedItem("User");
        cboUsotip.setSelectedItem("Auxiliar");
        cboSexUsu.setSelectedItem("Não Informado");
        cboFilter.setSelectedItem("Todos");
        btnUsuDelete.setEnabled(false);
        btnUsuCreate.setEnabled(false);
        btnUsuUpdate.setEnabled(false);
        btnAltFot.setEnabled(false);
        btnAltFot.setVisible(false);
        jdcDatUsu.setDateFormatString("yyyy-MM-dd");
        pesquisa_inic();

        cboUsotip.setModel(new DefaultComboBoxModel());

        ent3 = "/com/prjmanutencao/telas/imagens_fundo/sem_imagem1.jpg";

        ImageIcon icon = new ImageIcon(getClass().getResource(ent3));

        icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
        lblFotUsu.setIcon(icon);

        ZonedDateTime zdtNow = ZonedDateTime.now();
        jdcDatUsu.setDate(Timestamp.from(Instant.from(zdtNow)));
    }

    /**
     * Classe `DocumentSizeFilter` que estende `DocumentFilter` para implementar
     * restrições de tamanho máximo em documentos editáveis. Este filtro impede
     * que o número total de caracteres em um documento ultrapasse um limite
     * especificado (maxCharacters).
     */
    class DocumentSizeFilter extends DocumentFilter {

        int maxCharacters;
        boolean DEBUG = false;

        public DocumentSizeFilter(int maxChars) {
            maxCharacters = maxChars;
        }

        /**
         * Método `insertString` sobrescrito da classe `DocumentFilter`. Ele é
         * responsável por limitar a quantidade de caracteres que podem ser
         * inseridos em um documento. Caso o limite máximo de caracteres seja
         * excedido, a inserção é rejeitada e um alerta sonoro é emitido.
         *
         * @param fb Objeto `FilterBypass` que permite modificar diretamente o
         * conteúdo do documento.
         * @param offs Posição no documento onde o texto será inserido.
         * @param str Texto que está sendo inserido.
         * @param a Conjunto de atributos associados ao texto inserido.
         * @throws BadLocationException Exceção lançada se a posição onde o
         * texto seria inserido for inválida.
         */
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

        /**
         * Método sobrescrito `replace` da classe `DocumentFilter`. Este método
         * é utilizado para substituir caracteres no documento de forma
         * controlada, garantindo que a substituição respeite o limite máximo de
         * caracteres permitido.
         *
         * @param fb Objeto `FilterBypass` que permite modificar diretamente o
         * conteúdo do documento.
         * @param offs Posição inicial onde a substituição será realizada.
         * @param length Número de caracteres que serão substituídos.
         * @param str Texto que será inserido como substituto.
         * @param a Conjunto de atributos associados ao texto substituto.
         * @throws BadLocationException Exceção lançada caso a posição
         * especificada no documento seja inválida.
         */
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
     * Método `contagem`. Configura os documentos associados a diversos campos
     * de texto (`txtUsuId`, `txtUsoNom`, etc.), aplicando filtros de tamanho
     * máximo em cada campo. Isso garante que os usuários não possam inserir
     * mais caracteres do que o limite definido.
     */
    private void contagem() {
        DefaultStyledDocument doc, doc1, doc2, doc3, doc4;

        doc = new DefaultStyledDocument();
        doc1 = new DefaultStyledDocument();
        doc2 = new DefaultStyledDocument();
        doc3 = new DefaultStyledDocument();
        doc4 = new DefaultStyledDocument();

        doc.setDocumentFilter(new DocumentSizeFilter(10));
        doc1.setDocumentFilter(new DocumentSizeFilter(30));
        doc2.setDocumentFilter(new DocumentSizeFilter(20));
        doc3.setDocumentFilter(new DocumentSizeFilter(15));
        doc4.setDocumentFilter(new DocumentSizeFilter(30));

        txtUsuId.setDocument(doc);
        txtUsoNom.setDocument(doc1);
        txtUsoLog.setDocument(doc2);
        txtUsuSen.setDocument(doc3);
        txtUsuOutros.setDocument(doc4);

    }

    /**
     * Método `alinhar`. Responsável por configurar a largura das colunas,
     * centralizar os dados e personalizar o alinhamento do cabeçalho da tabela
     * `tblPeUsu`. Garante uma apresentação visual adequada e alinhada para os
     * dados exibidos na interface.
     */
    private void alinhar() {

        int width = 90, width1 = 200, width2 = 190, width3 = 240;

        JTableHeader header = tblPeUsu.getTableHeader();
        DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        tblPeUsu.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPeUsu.getColumnModel().getColumn(0).setPreferredWidth(width);
        tblPeUsu.getColumnModel().getColumn(1).setPreferredWidth(width1);
        tblPeUsu.getColumnModel().getColumn(2).setPreferredWidth(width2);
        tblPeUsu.getColumnModel().getColumn(3).setPreferredWidth(width3);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
        for (int i = 0; i < tblPeUsu.getColumnCount(); i++) {
            tblPeUsu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }

    }

    /**
     * Método `consultar`. Responsável por executar uma consulta no banco de
     * dados para obter informações do usuário com base em um identificador
     * parcial (`iduser`). Preenche os campos da interface com os dados
     * recuperados ou redefine os valores padrão caso o usuário não seja
     * encontrado.
     */
    private void consultar() {

        txtNomDir.setText(null);

        String sql = "select * from usuarios where iduser like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, pesq + "%");
            rs = pst.executeQuery();
            if (rs.next()) {
                txtUsoNom.setText(rs.getString(2));
                cboUsuPer.setSelectedItem(rs.getString(3));
                cboCadAreas.setSelectedItem(rs.getString(4));
                outros = (rs.getString(5));
                cboUsotip.addItem(outros);
                cboUsotip.setSelectedItem(rs.getString(5));
                txtUsoLog.setText(rs.getString(6));
                txtUsuSen.setText(rs.getString(7));
                txtNomDir.setText(rs.getString(8));
                nom_arq1 = (rs.getString(8));
                ent2 = (rs.getString(9));
                cboSexUsu.setSelectedItem(rs.getString(10));
                jdcDatUsu.setDate(rs.getDate(11));

                receber();
                btnUsuCreate.setEnabled(false);
                btnUsuDelete.setEnabled(true);
                btnUsuUpdate.setEnabled(true);
                btnAltFot.setVisible(true);
                alinhar();
            } else {

                btnUsuCreate.setEnabled(true);
                btnUsuDelete.setEnabled(false);
                btnUsuUpdate.setEnabled(false);
                btnAltFot.setVisible(false);
                pesquisar_usu();
                txtPesUsu.setText("");
                txtUsoNom.setText("");
                txtUsoLog.setText("");
                txtUsuSen.setText("");
                txtUsoNom.setText("");
                txtUsoLog.setText("");
                txtUsuSen.setText("");
                txtNomDir.setText(null);
                txtIdaUsu.setText(null);
                jdcDatUsu.setDate(null);
                cboUsuPer.setSelectedItem("User");
                cboUsotip.setSelectedItem("Auxiliar");
                cboSexUsu.setSelectedItem("Não Informado");
                cboCadAreas.setSelectedItem("A e B (Alimentos e Bebidas)");
                alinhar();

            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `comparar`. Responsável por verificar se o ID de usuário
     * especificado no campo `txtUsuId` já existe no banco de dados. Caso o ID
     * seja encontrado, o método desativa os campos e botões relacionados para
     * impedir alterações. Caso contrário, reativa os campos e botões para
     * permitir o cadastro ou modificação.
     */
    private void comparar() {

        String sql = "select iduser from usuarios where iduser = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUsuId.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtUsoNom.setEnabled(false);
                txtUsoLog.setEnabled(false);
                txtUsuSen.setEnabled(false);
                txtNomDir.setEnabled(false);
                txtIdaUsu.setEnabled(false);
                cboUsuPer.setEnabled(false);
                cboUsotip.setEnabled(false);
                cboSexUsu.setEnabled(false);
                cboCadAreas.setEnabled(false);
                jdcDatUsu.setEnabled(false);
                btnUsuFot.setEnabled(false);
                btnArqFotUsu.setEnabled(false);
                btnUsuUpdate.setEnabled(false);
                btnUsuDelete.setEnabled(false);
                btnUsuCreate.setEnabled(false);
                alinhar();

            } else {
                pesquisar_usu();
                txtPesUsu.setText(null);
                txtUsoNom.setEnabled(true);
                txtUsoLog.setEnabled(true);
                txtUsuSen.setEnabled(true);
                txtNomDir.setEnabled(true);
                txtIdaUsu.setEnabled(true);
                cboUsuPer.setEnabled(true);
                cboUsotip.setEnabled(true);
                cboSexUsu.setEnabled(true);
                cboCadAreas.setEnabled(true);
                jdcDatUsu.setEnabled(true);
                btnUsuFot.setEnabled(true);
                btnArqFotUsu.setEnabled(true);
                btnUsuCreate.setEnabled(true);
                ent2 = null;
                limpar1();
                alinhar();
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `pesquisa_inic`. Realiza uma consulta inicial no banco de dados
     * para obter informações sobre todos os usuários cadastrados. Os dados
     * recuperados são exibidos na tabela `tblPeUsu`.
     */
    private void pesquisa_inic() {

        String sql = "select iduser as 'R.E', nome_usuario as 'Nome', area_usuario as 'Área', tipo_usuario as 'Tipo' from usuarios";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tblPeUsu.setModel(DbUtils.resultSetToTableModel(rs));
            alinhar();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `pesquisar_usu`. Realiza uma busca no banco de dados para
     * encontrar usuários de acordo com o filtro selecionado no ComboBox
     * `cboFilter` e o texto de pesquisa fornecido no campo `txtPesUsu`. Os
     * resultados são exibidos na tabela `tblPeUsu`.
     */
    private void pesquisar_usu() {
        String str;
        str = cboFilter.getSelectedItem().toString();

        if (str.equals("Todos")) {

            String sql = "select iduser as 'R.E', nome_usuario as 'Nome', area_usuario as 'Área', tipo_usuario as 'Tipo' from usuarios where iduser like ? or nome_usuario like ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtPesUsu.getText() + "%");
                pst.setString(2, txtPesUsu.getText() + "%");
                rs = pst.executeQuery();
                tblPeUsu.setModel(DbUtils.resultSetToTableModel(rs));
                alinhar();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {

            String sql = "select iduser as 'R.E', nome_usuario as 'Nome', area_usuario as 'Área', tipo_usuario as 'Tipo' from usuarios where area_usuario = ? and iduser like ? or area_usuario = ? and nome_usuario like ? ";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, cboFilter.getSelectedItem().toString());
                pst.setString(2, txtPesUsu.getText() + "%");
                pst.setString(3, cboFilter.getSelectedItem().toString());
                pst.setString(4, txtPesUsu.getText() + "%");

                rs = pst.executeQuery();
                tblPeUsu.setModel(DbUtils.resultSetToTableModel(rs));
                alinhar();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }

    /**
     * Método `setar_usu`. Responsável por configurar os campos da interface com
     * as informações do usuário selecionado na tabela `tblPeUsu`. Realiza
     * consultas adicionais para obter dados detalhados, ajusta a apresentação
     * visual e reativa os elementos necessários para edição.
     */
    private void setar_usu() {
        cboSetor();
        int setar = tblPeUsu.getSelectedRow();
        txtUsuId.setText(tblPeUsu.getModel().getValueAt(setar, 0).toString());
        pesq = (tblPeUsu.getModel().getValueAt(setar, 0).toString());
        consultar();
        idade_usu();
        cboFilter.setSelectedItem("Todos");
        pesquisa_inic();
        alinhar();

        txtPesUsu.setText(null);
        txtUsoNom.setEnabled(true);
        txtUsoLog.setEnabled(true);
        txtUsuSen.setEnabled(true);
        txtUsoNom.setEnabled(true);
        txtUsoLog.setEnabled(true);
        txtUsuSen.setEnabled(true);
        txtNomDir.setEnabled(true);
        txtIdaUsu.setEnabled(true);
        cboUsuPer.setEnabled(true);
        cboUsotip.setEnabled(true);
        cboSexUsu.setEnabled(true);
        cboCadAreas.setEnabled(true);
        jdcDatUsu.setEnabled(true);
        btnUsuFot.setEnabled(true);
        btnArqFotUsu.setEnabled(true);
    }

    /**
     * Método `idade_usu`. Calcula a idade do usuário com base na data de
     * nascimento armazenada no banco de dados (`nasc_usu`) e exibe o resultado
     * no campo de texto `txtIdaUsu`.
     *
     * @param iduser O identificador único do usuário é utilizado como parâmetro
     * na consulta SQL.
     */
    private void idade_usu() {

        String sql = "select YEAR(CURDATE()) - YEAR(nasc_usu) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(nasc_usu, '%m%d')) AS idade from usuarios where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUsuId.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdaUsu.setText(rs.getString(1) + " Anos");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `search_file`. Responsável por permitir ao usuário selecionar e
     * carregar uma imagem de arquivo do sistema. O arquivo selecionado é
     * exibido no rótulo `lblFotUsu` com as dimensões ajustadas. Em caso de
     * erro, uma imagem padrão é carregada no lugar.
     */
    private void search_file() {

        try {
            JFileChooser file = new JFileChooser();
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagem em JPEG ", "jpg");
            file.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
            file.setFileFilter(filtro);
            file.setDialogTitle("Carregar Foto");
            file.showOpenDialog(this);
            ent2 = file.getSelectedFile().getPath();
            txtNomDir.setText(file.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""));

            ImageIcon icon = new ImageIcon(ent2);
            icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
            lblFotUsu.setIcon(icon);
            btnAltFot.setEnabled(true);

        } catch (HeadlessException e) {
            ImageIcon icon = new ImageIcon(getClass().getResource(ent3));
            icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
            lblFotUsu.setIcon(icon);
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método upload_file.
     *
     * Realiza o envio de um arquivo para um servidor FTP.
     *
     * Conecta ao servidor usando os dados de acesso e porta definidos. Cria o
     * diretório de destino, entra em modo passivo e define o tipo de arquivo
     * como binário.
     *
     * O arquivo a ser enviado pode vir de duas fontes: - ent2: arquivo local
     * selecionado pelo usuário. - ent3: recurso interno da aplicação.
     *
     * Utiliza FileInputStream para ler o arquivo e envia usando o método
     * storeFile. Em caso de erro de conexão, leitura ou URI inválida, exibe uma
     * mensagem de erro.
     */
    private void upload_file() {

        String dir = txtNomDir.getText();
        FTPClient ftp = new FTPClient();

        try {

            ftp.connect(acesso, porta);

            ftp.login(log, sen);

            ftp.makeDirectory(dire);
            ftp.changeWorkingDirectory(dire);
            ftp.enterLocalPassiveMode(); // Modo passivo
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            if (ent2 != null) {
                try (FileInputStream arqEnviar = new FileInputStream(ent2)) {
                    if (ftp.storeFile(dir, arqEnviar)) {
                    }
                    arqEnviar.close();
                }
            } else {

                ImageIcon icon = new ImageIcon(getClass().getResource(ent3));
                icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
                lblFotUsu.setIcon(icon);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método `receber`. Responsável por baixar um arquivo do servidor FTP e
     * salvá-lo localmente. O arquivo baixado é exibido no rótulo `lblFotUsu`
     * com as dimensões ajustadas. Em caso de erro ou falha no download, uma
     * imagem padrão é carregada.
     */
    private void receber() {

        FTPClient ftp = new FTPClient();
//        String nom_arq;
        nom_arq1 = txtNomDir.getText();
        try {

            ftp.connect(acesso, porta);

            ftp.login(log, sen);
            String caminhoArquivo = dire.concat("/").concat(nom_arq1);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            if (ftp.retrieveFile(caminhoArquivo, outputStream)) {
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                ImageIcon icon = new ImageIcon(ImageIO.read(inputStream));
                icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
                lblFotUsu.setIcon(icon);
            } else {

                ImageIcon icon = new ImageIcon(getClass().getResource(ent3));
                icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
                lblFotUsu.setIcon(icon);

            }
            // Desconecta do servidor
            ftp.logout();
            ftp.disconnect();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `deletar1`. Responsável por excluir um arquivo do servidor FTP com
     * base no nome especificado no campo de texto `txtNomDir`. Caso o arquivo
     * não possa ser excluído ou ocorra algum erro durante o processo, uma
     * mensagem de erro é exibida ao usuário.
     */
    private void deletar1() {
        String nom_arq2 = null;
        nom_arq2 = txtNomDir.getText();

        FTPClient ftp = new FTPClient();

        try {
            ftp.connect(acesso, porta);

            ftp.login(log, sen);

            ftp.changeWorkingDirectory(dire);
            String[] arq = ftp.listNames();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.deleteFile(nom_arq2);

            ftp.disconnect();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `servicos`. Converte a data selecionada no componente de data
     * `jdcDatUsu` para o formato `yyyy-MM-dd` e armazena o resultado na
     * variável `data` da classe. Em caso de erro, exibe uma mensagem de erro ao
     * usuário.
     */
    private void servicos() {

        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date pega;
            pega = jdcDatUsu.getDate();
            this.data = formato.format(pega);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `cboAdicionar`. Responsável por definir o valor da variável
     * `outros` com base na seleção atual do ComboBox `cboUsotip`. Caso a
     * seleção seja "Outro", o valor será extraído do campo de texto
     * `txtUsuOutros`. Caso contrário, o valor será o texto atualmente
     * selecionado no ComboBox `cboUsotip`.
     */
    private void cboAdicionar() {
        String str = "";
        str = cboUsotip.getSelectedItem().toString();
        if (str.equals("Outro")) {
            outros = txtUsuOutros.getText();
        } else {
            outros = cboUsotip.getSelectedItem().toString();
        }
    }

    /**
     * Método `adicionar`. Responsável por realizar o cadastro de um novo
     * usuário no banco de dados, tratando os dados inseridos e configurando a
     * foto do usuário para upload ao servidor FTP. Realiza validações
     * obrigatórias antes de executar o registro.
     */
    private void adicionar() {
        cboAdicionar();
        if (ent2 != null) {
            Random aleatorio = new Random();
            int valor = aleatorio.nextInt(300000) + 1;
            txtNomDir.setText(valor + "");
            dir = txtNomDir.getText();
            dir = txtNomDir.getText();
            String fileName = ent2;
            int a = fileName.lastIndexOf('.');
            if (a >= 0) {
                fe = fileName.substring(a + 1);
            } else {
                txtNomDir.setText(null);
            }
        }

        servicos();
        String sql = "insert into usuarios(iduser, nome_usuario, perfil_usuario, area_usuario, tipo_usuario, login_usuario, senha_usuario, nome_foto, foto_usu, sexo_usu, nasc_usu) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUsuId.getText());
            pst.setString(2, txtUsoNom.getText());
            pst.setString(3, cboUsuPer.getSelectedItem().toString());
            pst.setString(4, cboCadAreas.getSelectedItem().toString());
            pst.setString(5, outros);
            pst.setString(6, txtUsoLog.getText());
            pst.setString(7, txtUsuSen.getText());
            pst.setString(8, txtNomDir.getText() + (".") + fe);
            pst.setString(9, acesso + dir + (".") + fe);
            txtNomDir.setText(dir + (".") + fe);
            pst.setString(10, cboSexUsu.getSelectedItem().toString());
            pst.setString(11, data);
            if ((txtUsuSen.getText().isEmpty()) || (txtUsuId.getText().isEmpty()) || (txtUsoNom.getText().isEmpty()) || (txtUsoLog.getText().isEmpty())) {
                txtNomDir.setText(null);
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");

            } else {
                int Adicionar = pst.executeUpdate();
                if (Adicionar > 0) {
                    upload_file();
                    JOptionPane.showMessageDialog(null, "Usuário Cadastrado com Sucesso!");
                    limpar();
                    conexao.close();
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar Usuario!");
                    limpar();
                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `cboSetor`. Configura os itens disponíveis no ComboBox `cboUsotip`
     * com base na seleção feita no ComboBox `cboCadAreas`. Cada setor
     * corresponde a uma lista específica de cargos ou funções.
     */
    private void cboSetor() {

        String combo;

        combo = cboCadAreas.getSelectedItem().toString();

        switch (combo) {
            case "A e B (Alimentos e Bebidas)":
                cboUsotip.setModel(new DefaultComboBoxModel());
                String cbo[] = {"Gerente", "Chef de Cozinha", "Subchefe de Cozinha", "Segundo Cozinheiro ", "Auxiliar de Cozinha", "Commis de Cozinha", "Primeiro Maitre", "Segundo Maitre", "Terceiro Maitre", "Sommelier", "Chefe de Fila", "Garçom", "Commis de Salão", "Chefe de Copa Cambuzeiro", "Copeiro", "Auxiliar de Copa", "Barman/Bargirl", "Garçom de Bar", "Outro"};

                for (String string : cbo) {
                    cboUsotip.addItem(string);
                }
                break;
            case "Administrativo":
                cboUsotip.setModel(new DefaultComboBoxModel());
                String cbo1[] = {"Financeiro", "Marketing", "Recursos Humanos", "Comercial", "Outro"};
                for (String string : cbo1) {
                    cboUsotip.addItem(string);
                }
                break;
            case "Eventos":
                cboUsotip.setModel(new DefaultComboBoxModel());
                String cbo2[] = {"Gerente de Eventos", "Supervisor de Eventos", "Outro"};
                for (String string : cbo2) {
                    cboUsotip.addItem(string);
                }
                break;
            case "Governança":
                cboUsotip.setModel(new DefaultComboBoxModel());
                String cbo3[] = {"Governanta Executiva", "Supervisor de Andar", "Camareira", "Arrumador", "Auxiliar de Limpeza", "Outro"};
                for (String string : cbo3) {
                    cboUsotip.addItem(string);
                }
                break;
            case "Manutenção":
                cboUsotip.setModel(new DefaultComboBoxModel());
                String cbo4[] = {"Gerente de Manutenção ", "Operacional", "Técnico - Manutenção", "Técnico - Elétrica", "Técnico - Hidráulica", "Técnico - Civil", "Técnico - Refrigeração", "Auxiliar", "Amoxarife", "Auxiliar de Amoxarifado", "Outro"};
                for (String string : cbo4) {
                    cboUsotip.addItem(string);
                }
                break;
            case "Mordomia":
                cboUsotip.setModel(new DefaultComboBoxModel());
                String cbo5[] = {"Mordomo", "Auxiliar de Mordomia", "Outro"};
                for (String string : cbo5) {
                    cboUsotip.addItem(string);
                }
                break;
            case "Recepção":
                cboUsotip.setModel(new DefaultComboBoxModel());
                String cbo6[] = {"Gerente de Hospedagem", "Chefe da Recepção", "Recepcionista", "Concierge", "Chefe de Telefonia", "Chefe de Reservas", "Atendente de Reservas", "Telefonista", "Mensageiro", "Outro"};
                for (String string : cbo6) {
                    cboUsotip.addItem(string);
                }
                break;
            case "Segurança":
                cboUsotip.setModel(new DefaultComboBoxModel());//"","","","","","","","",""
                String cbo7[] = {"Chefe de Segurança", "Supervisor de Segurança", "Segurança", "Outro"};
                for (String string : cbo7) {
                    cboUsotip.addItem(string);
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Nenhuma Opção Encontada");
        }

    }

    /**
     * Método `alterar`. Realiza alterações no cadastro de um usuário existente
     * no banco de dados. Solicita confirmação do usuário antes de aplicar as
     * alterações e valida os campos obrigatórios.
     */
    private void alterar() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que Deseja Realizar Alterações neste Cadastro?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            cboAdicionar();

            java.util.Date pega = jdcDatUsu.getDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            this.data = formato.format(pega);
            String sql = "update usuarios set nome_usuario=?, perfil_usuario=?, tipo_usuario=?, area_usuario = ?, login_usuario=?, senha_usuario=?,sexo_usu=?, nasc_usu=? where iduser=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUsoNom.getText());
                pst.setString(2, cboUsuPer.getSelectedItem().toString());
                pst.setString(3, outros);
                pst.setString(4, cboCadAreas.getSelectedItem().toString());
                pst.setString(5, txtUsoLog.getText());
                pst.setString(6, txtUsuSen.getText());
                pst.setString(7, cboSexUsu.getSelectedItem().toString());
                pst.setString(8, data);
                pst.setString(9, txtUsuId.getText());

                if ((txtUsuSen.getText().isEmpty()) || (txtUsuId.getText().isEmpty()) || (txtUsoNom.getText().isEmpty()) || (txtUsoLog.getText().isEmpty())) {

                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {

                    int alterar1 = pst.executeUpdate();
                    if (alterar1 > 0) {
                        JOptionPane.showMessageDialog(null, "Dados do Usuário Alterados com Sucesso!");
                        ImageIcon icon = new ImageIcon(getClass().getResource(ent3));
                        icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), 60));
                        lblFotUsu.setIcon(icon);
                        limpar();
                        conexao.close();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Alterar os Dados!");
                        conexao.close();
                        limpar();
                    }
                }

            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    /**
     * Método `altera_foto`. Responsável por atualizar as informações da foto de
     * um usuário existente no banco de dados. Verifica o formato do arquivo
     * selecionado, salva o nome e o caminho no banco, e faz o upload da imagem
     * para o servidor FTP. Exibe mensagens de sucesso ou erro conforme
     * necessário.
     */
    private void altera_foto() {

        String fe1 = null;
        String fileName2 = ent2;

        dir = txtNomDir.getText();

        if (ent2 != null) {
            int a = fileName2.lastIndexOf('.');
            if (a >= 0) {
                fe1 = fileName2.substring(a + 1);

            } else {
            }

        }

        String sql = "update usuarios set nome_foto=?, foto_usu=? where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomDir.getText() + (".") + fe1);
            pst.setString(2, acesso + dir + (".") + fe1);
            txtNomDir.setText(dir + (".") + fe1);
            pst.setString(3, txtUsuId.getText());
            if ((txtUsuSen.getText().isEmpty()) || (txtUsuId.getText().isEmpty()) || (txtUsoNom.getText().isEmpty()) || (txtUsoLog.getText().isEmpty()) || (txtNomDir.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");

            } else {
                int Adicionar = pst.executeUpdate();
                if (Adicionar > 0) {
                    btnAltFot.setEnabled(false);
                    upload_file();
                    JOptionPane.showMessageDialog(null, "Foto Salva com Sucesso!");

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar Usuario!");
                    limpar();
                }
            }

        } catch (HeadlessException | SQLException e) {
        }

    }

    /**
     * Método `remover`. Responsável por excluir um registro de usuário do banco
     * de dados com base no ID especificado. Antes de realizar a exclusão,
     * verifica se os campos obrigatórios estão preenchidos e solicita
     * confirmação do usuário. Também exclui a foto associada ao usuário do
     * servidor FTP.
     */
    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que Deseja Remover esse Usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from usuarios where iduser=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUsuId.getText());

                if ((txtUsuSen.getText().isEmpty()) || (txtUsuId.getText().isEmpty()) || (txtUsoNom.getText().isEmpty()) || (txtUsoLog.getText().isEmpty()) || (txtNomDir.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");

                } else {
                    int apagado = pst.executeUpdate();
                    if (apagado > 0) {
                        deletar1();
                        JOptionPane.showMessageDialog(null, "Usuário Removido com Sucesso!");
                        limpar();
                        dispose();

                    }
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Método `limpar`. Responsável por redefinir todos os campos e componentes
     * da interface do usuário para seus valores padrão. Também atualiza a
     * exibição da foto para uma imagem padrão.
     */
    private void limpar() {
        txtUsoNom.setText("");
        txtUsoLog.setText("");
        txtUsuSen.setText("");
        txtUsuId.setText("");
        txtUsoNom.setText("");
        txtUsoLog.setText("");
        txtUsuSen.setText("");
        txtNomDir.setText("");
        txtIdaUsu.setText("");
        cboUsuPer.setSelectedItem("User");
        cboUsotip.setSelectedItem("Auxiliar");
        cboSexUsu.setSelectedItem("Não Informado");
        cboCadAreas.setSelectedItem("A e B (Alimentos e Bebidas)");
        cboFilter.setSelectedItem("Todos");
        ZonedDateTime zdtNow = ZonedDateTime.now();
        jdcDatUsu.setDate(Timestamp.from(Instant.from(zdtNow)));
        ImageIcon icon = new ImageIcon(getClass().getResource(ent3));
        icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
        lblFotUsu.setIcon(icon);

    }

    /**
     * Método `limpar`. Responsável por redefinir todos os campos e componentes
     * da interface do usuário para seus valores padrão ecerto o campo
     * 'txtUsuId'. Também atualiza a exibição da foto para uma imagem padrão.
     */
    private void limpar1() {
        
        btnUsuUpdate.setEnabled(false);
        btnUsuDelete.setEnabled(false);
        txtUsoNom.setText("");
        txtUsoLog.setText("");
        txtUsuSen.setText("");
        txtUsoNom.setText("");
        txtUsoLog.setText("");
        txtUsuSen.setText("");
        txtNomDir.setText("");
        txtIdaUsu.setText("");
        cboUsuPer.setSelectedItem("User");
        cboUsotip.setSelectedItem("Auxiliar");
        cboSexUsu.setSelectedItem("Não Informado");
        cboCadAreas.setSelectedItem("A e B (Alimentos e Bebidas)");
        cboFilter.setSelectedItem("Todos");
        ZonedDateTime zdtNow = ZonedDateTime.now();
        jdcDatUsu.setDate(Timestamp.from(Instant.from(zdtNow)));
        ImageIcon icon = new ImageIcon(getClass().getResource(ent3));
        icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
        lblFotUsu.setIcon(icon);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        txtUsuId = new javax.swing.JTextField();
        txtUsoNom = new javax.swing.JTextField();
        txtUsoLog = new javax.swing.JTextField();
        cboUsuPer = new javax.swing.JComboBox<>();
        txtUsuSen = new javax.swing.JTextField();
        cboUsotip = new javax.swing.JComboBox<>();
        btnUsuCreate = new javax.swing.JButton();
        btnUsuUpdate = new javax.swing.JButton();
        btnUsuDelete = new javax.swing.JButton();
        lblFotUsu = new javax.swing.JLabel();
        btnUsuFot = new javax.swing.JButton();
        btnArqFotUsu = new javax.swing.JButton();
        txtNomDir = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cboSexUsu = new javax.swing.JComboBox<>();
        txtIdaUsu = new javax.swing.JTextField();
        btnAltFot = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jdcDatUsu = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPeUsu = new javax.swing.JTable();
        txtPesUsu = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cboCadAreas = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtUsuOutros = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cboFilter = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Sistema - Usuários");
        setPreferredSize(new java.awt.Dimension(1007, 672));
        setRequestFocusEnabled(false);
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

        txtUsuId.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtUsuId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsuIdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuIdKeyTyped(evt);
            }
        });
        getContentPane().add(txtUsuId);
        txtUsuId.setBounds(256, 185, 232, 28);

        txtUsoNom.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtUsoNom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsoNom.setText("     ");
        txtUsoNom.setMinimumSize(new java.awt.Dimension(4, 21));
        txtUsoNom.setPreferredSize(new java.awt.Dimension(65, 15));
        txtUsoNom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsoNomKeyTyped(evt);
            }
        });
        getContentPane().add(txtUsoNom);
        txtUsoNom.setBounds(256, 267, 232, 28);

        txtUsoLog.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtUsoLog.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsoLog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsoLogKeyTyped(evt);
            }
        });
        getContentPane().add(txtUsoLog);
        txtUsoLog.setBounds(256, 349, 232, 28);

        cboUsuPer.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cboUsuPer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Superadmin", "Admin", "User" }));
        getContentPane().add(cboUsuPer);
        cboUsuPer.setBounds(738, 188, 232, 28);

        txtUsuSen.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtUsuSen.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuSen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuSenKeyTyped(evt);
            }
        });
        getContentPane().add(txtUsuSen);
        txtUsuSen.setBounds(738, 349, 232, 28);

        cboUsotip.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cboUsotip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboUsotipActionPerformed(evt);
            }
        });
        getContentPane().add(cboUsotip);
        cboUsotip.setBounds(738, 267, 232, 28);

        btnUsuCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48761_file_add_file_upload_add_upload.png"))); // NOI18N
        btnUsuCreate.setToolTipText("Adicionar");
        btnUsuCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuCreateActionPerformed(evt);
            }
        });
        getContentPane().add(btnUsuCreate);
        btnUsuCreate.setBounds(565, 554, 64, 64);

        btnUsuUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48763_edit_edit_file_file.png"))); // NOI18N
        btnUsuUpdate.setToolTipText("Alterar");
        btnUsuUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(btnUsuUpdate);
        btnUsuUpdate.setBounds(706, 554, 64, 64);

        btnUsuDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48762_delete_delete file_delete_remove_delete_file_remove_remove_file.png"))); // NOI18N
        btnUsuDelete.setToolTipText("Remover");
        btnUsuDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnUsuDelete);
        btnUsuDelete.setBounds(853, 554, 64, 64);

        lblFotUsu.setBackground(new java.awt.Color(102, 102, 102));
        lblFotUsu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFotUsu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        lblFotUsu.setOpaque(true);
        getContentPane().add(lblFotUsu);
        lblFotUsu.setBounds(58, 416, 190, 190);

        btnUsuFot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48744_camera_camera.png"))); // NOI18N
        btnUsuFot.setToolTipText("Camera");
        btnUsuFot.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuFot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuFotActionPerformed(evt);
            }
        });
        getContentPane().add(btnUsuFot);
        btnUsuFot.setBounds(280, 418, 64, 64);

        btnArqFotUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48769_folder_empty_folder_empty.png"))); // NOI18N
        btnArqFotUsu.setToolTipText("Arquivos");
        btnArqFotUsu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArqFotUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArqFotUsuActionPerformed(evt);
            }
        });
        getContentPane().add(btnArqFotUsu);
        btnArqFotUsu.setBounds(380, 418, 64, 64);

        txtNomDir.setEditable(false);
        txtNomDir.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomDir.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNomDir.setEnabled(false);
        getContentPane().add(txtNomDir);
        txtNomDir.setBounds(270, 574, 180, 28);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nome");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(334, 600, 50, 15);

        cboSexUsu.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cboSexUsu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não Informado", "Masculino", "Feminino" }));
        getContentPane().add(cboSexUsu);
        cboSexUsu.setBounds(256, 226, 232, 28);

        txtIdaUsu.setEditable(false);
        txtIdaUsu.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtIdaUsu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdaUsu);
        txtIdaUsu.setBounds(408, 308, 80, 28);

        btnAltFot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48783_add_add_notification.png"))); // NOI18N
        btnAltFot.setToolTipText("Alterar Foto");
        btnAltFot.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltFot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltFotActionPerformed(evt);
            }
        });
        getContentPane().add(btnAltFot);
        btnAltFot.setBounds(330, 498, 64, 64);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48783_add_add_notification.png"))); // NOI18N
        jLabel3.setToolTipText("Alterar Foto");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)));
        jLabel3.setEnabled(false);
        jLabel3.setOpaque(true);
        getContentPane().add(jLabel3);
        jLabel3.setBounds(330, 498, 64, 64);

        jLabel4.setText("Fotos em Formato jpg");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(92, 606, 130, 14);

        ImageIcon icon = new ImageIcon("C:\\Users\\fabio\\OneDrive\\Área de Trabalho\\NetBeansProjects\\prjManutencao1\\src\\com\\prjmanutencao\\icones\\data.png");
        jdcDatUsu.setIcon(icon);
        jdcDatUsu.setToolTipText("");
        jdcDatUsu.setDateFormatString("");
        jdcDatUsu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jdcDatUsu.setIcon(null);
        jdcDatUsu.setMinSelectableDate(new java.util.Date(-62135755111000L));
        getContentPane().add(jdcDatUsu);
        jdcDatUsu.setBounds(256, 308, 140, 28);

        tblPeUsu = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblPeUsu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "R.E", "Nome", "Área", "Tipo"
            }
        ));
        tblPeUsu.getTableHeader().setReorderingAllowed(false);
        tblPeUsu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPeUsuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPeUsu);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(510, 440, 461, 90);

        txtPesUsu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesUsu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesUsuKeyReleased(evt);
            }
        });
        getContentPane().add(txtPesUsu);
        txtPesUsu.setBounds(819, 410, 150, 28);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/Retângulo 538.png"))); // NOI18N
        getContentPane().add(jLabel5);
        jLabel5.setBounds(500, 540, 480, 90);

        cboCadAreas.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cboCadAreas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A e B (Alimentos e Bebidas)", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança", " " }));
        cboCadAreas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCadAreasActionPerformed(evt);
            }
        });
        getContentPane().add(cboCadAreas);
        cboCadAreas.setBounds(738, 228, 232, 28);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel7);
        jLabel7.setBounds(724, 185, 258, 34);

        jLabel8.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 70));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("*Login:");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel8);
        jLabel8.setBounds(154, 346, 90, 34);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/fotAqui.png"))); // NOI18N
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel9);
        jLabel9.setBounds(40, 400, 424, 230);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel11);
        jLabel11.setBounds(724, 225, 258, 34);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel13);
        jLabel13.setBounds(724, 346, 258, 34);

        txtUsuOutros.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtUsuOutros.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuOutros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuOutrosKeyTyped(evt);
            }
        });
        getContentPane().add(txtUsuOutros);
        txtUsuOutros.setBounds(738, 308, 232, 28);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel12);
        jLabel12.setBounds(724, 305, 258, 34);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel14);
        jLabel14.setBounds(242, 182, 258, 34);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel15);
        jLabel15.setBounds(242, 223, 258, 34);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel16);
        jLabel16.setBounds(242, 264, 258, 34);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel17);
        jLabel17.setBounds(242, 305, 258, 34);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel18);
        jLabel18.setBounds(242, 346, 258, 34);

        jLabel20.setFont(new java.awt.Font("Arial Black", 0, 48)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 80));
        jLabel20.setText("Cadastro de Usuários");
        getContentPane().add(jLabel20);
        jLabel20.setBounds(170, 60, 570, 64);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48812_manage_school_icon.png"))); // NOI18N
        getContentPane().add(jLabel21);
        jLabel21.setBounds(740, 30, 128, 128);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Rectangle 8.png"))); // NOI18N
        jLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel22);
        jLabel22.setBounds(724, 264, 258, 34);

        jLabel23.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 70));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("*Campos Obrigatórios");
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel23);
        jLabel23.setBounds(480, 380, 160, 16);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 50));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        jLabel33.setText("Usuários");
        getContentPane().add(jLabel33);
        jLabel33.setBounds(520, 410, 100, 28);

        jLabel24.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 70));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("*R.E:");
        jLabel24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel24);
        jLabel24.setBounds(183, 182, 60, 34);

        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "A e B (Alimentos e Bebidas)", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança" }));
        cboFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFilterActionPerformed(evt);
            }
        });
        getContentPane().add(cboFilter);
        cboFilter.setBounds(620, 410, 190, 28);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/tblUsuarios.png"))); // NOI18N
        getContentPane().add(jLabel19);
        jLabel19.setBounds(500, 400, 480, 136);

        jLabel25.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 70));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Sexo:");
        jLabel25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel25);
        jLabel25.setBounds(174, 223, 72, 34);

        jLabel26.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 70));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("*Nome:");
        jLabel26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel26);
        jLabel26.setBounds(154, 264, 90, 34);

        jLabel27.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 70));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("*Data de Nasc.:");
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel27);
        jLabel27.setBounds(54, 305, 190, 34);

        jLabel28.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 70));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("*Senha:");
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel28);
        jLabel28.setBounds(625, 346, 100, 34);

        jLabel29.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 70));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Área:");
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel29);
        jLabel29.setBounds(656, 225, 70, 34);

        jLabel30.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 70));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Tipo:");
        jLabel30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel30);
        jLabel30.setBounds(659, 264, 66, 34);

        jLabel31.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 70));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("*Outro:");
        jLabel31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel31);
        jLabel31.setBounds(635, 305, 90, 34);

        jLabel32.setFont(new java.awt.Font("Arial Black", 0, 22)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 70));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Perfil:");
        jLabel32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel32);
        jLabel32.setBounds(650, 185, 78, 34);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/usuarios1.jpg"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(1010, 672));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 650);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `cboUsotipActionPerformed`. Manipula o evento de seleção no
     * ComboBox `cboUsotip`. Habilita ou desabilita o campo de texto
     * `txtUsuOutros` com base na opção selecionada. Caso a seleção seja
     * "Outro", o campo é habilitado para entrada de texto; caso contrário, o
     * campo é desabilitado e limpo.
     *
     * @param evt Evento acionado ao interagir com o ComboBox.
     */
    private void cboUsotipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboUsotipActionPerformed

        String tipo;
        tipo = cboUsotip.getSelectedItem().toString();
        if (tipo.equals("Outro")) {
            txtUsuOutros.setEnabled(true);
        } else {
            txtUsuOutros.setEnabled(false);
            txtUsuOutros.setText("");
        }

    }//GEN-LAST:event_cboUsotipActionPerformed

    /**
     * Método `btnUsuCreateActionPerformed`. Manipula o evento de clique no
     * botão "Criar Usuário". Invoca o método `adicionar()` para registrar um
     * novo usuário no sistema.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnUsuCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuCreateActionPerformed

        adicionar();
    }//GEN-LAST:event_btnUsuCreateActionPerformed

    /**
     * Método `btnUsuUpdateActionPerformed`. Manipula o evento de clique no
     * botão "Atualizar Usuário". Invoca o método `alterar()` para realizar
     * modificações nos dados de um usuário existente.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnUsuUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuUpdateActionPerformed

        alterar();
    }//GEN-LAST:event_btnUsuUpdateActionPerformed

    /**
     * Método `btnUsuDeleteActionPerformed`. Manipula o evento de clique no
     * botão "Excluir Usuário". Invoca o método `remover()` para realizar a
     * exclusão de um usuário do banco de dados e tratar possíveis dependências,
     * como a remoção de sua foto do servidor FTP.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnUsuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuDeleteActionPerformed

        remover();
    }//GEN-LAST:event_btnUsuDeleteActionPerformed

    /**
     * Método `btnUsuFotActionPerformed`. Manipula o evento de clique no botão
     * "UsuFot". Abre uma nova janela para captura de imagens usando a classe
     * `TelaWebCamUsu`.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnUsuFotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuFotActionPerformed

        TelaWebCamUsu captura = new TelaWebCamUsu();
        captura.setVisible(true);
    }//GEN-LAST:event_btnUsuFotActionPerformed

    /**
     * Método `btnArqFotUsuActionPerformed`. Manipula o evento de clique no
     * botão "ArqFotUsu". Invoca o método `search_file()` para permitir que o
     * usuário selecione um arquivo de imagem do sistema.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnArqFotUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArqFotUsuActionPerformed

        search_file();
    }//GEN-LAST:event_btnArqFotUsuActionPerformed

    /**
     * Método `btnAltFotActionPerformed`. Manipula o evento de clique no botão
     * "Alterar Foto". Invoca o método `altera_foto()` para atualizar a foto do
     * usuário no banco de dados e realizar o upload da nova imagem para o
     * servidor FTP.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnAltFotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltFotActionPerformed

        altera_foto();
    }//GEN-LAST:event_btnAltFotActionPerformed

    /**
     * Método `txtPesUsuKeyReleased`. Manipula o evento de teclas liberadas
     * (KeyReleased) no campo de texto `txtPesUsu`. Invoca o método
     * `pesquisar_usu()` para realizar uma busca dinâmica baseada no texto
     * digitado.
     *
     * @param evt Evento acionado ao liberar uma tecla no campo de texto.
     */
    private void txtPesUsuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesUsuKeyReleased

        pesquisar_usu();
    }//GEN-LAST:event_txtPesUsuKeyReleased

    /**
     * Método `tblPeUsuMouseClicked`. Manipula o evento de clique do mouse na
     * tabela `tblPeUsu`. Ao clicar em uma linha da tabela, o método invoca o
     * `setar_usu()` para preencher os campos da interface com os dados do
     * usuário selecionado.
     *
     * @param evt Evento acionado pelo clique do mouse.
     */
    private void tblPeUsuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPeUsuMouseClicked

        setar_usu();
    }//GEN-LAST:event_tblPeUsuMouseClicked

    /**
     * Método `formInternalFrameOpened`. Manipula o evento de abertura da janela
     * interna. Realiza configurações iniciais, como desabilitar campos de
     * entrada e botões, definir um ícone para o componente de data `jdcDatUsu`
     * e configurar propriedades de tabela. Também invoca o método `cboSetor()`
     * para preparar o ComboBox com valores relevantes.
     *
     * @param evt Evento acionado ao abrir a janela interna.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        ImageIcon icon = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/data.png"));
        jdcDatUsu.setIcon(icon);

        tblPeUsu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txtUsoNom.setEnabled(false);
        txtUsoLog.setEnabled(false);
        txtUsuSen.setEnabled(false);
        txtUsoNom.setEnabled(false);
        txtUsoLog.setEnabled(false);
        txtUsuSen.setEnabled(false);
        txtNomDir.setEnabled(false);
        txtIdaUsu.setEnabled(false);
        txtUsuOutros.setEnabled(false);
        cboUsuPer.setEnabled(false);
        cboUsotip.setEnabled(false);
        cboSexUsu.setEnabled(false);
        cboCadAreas.setEnabled(false);
        jdcDatUsu.setEnabled(false);
        btnUsuFot.setEnabled(false);
        btnArqFotUsu.setEnabled(false);
        cboSetor();

    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método `txtUsuIdKeyReleased`. Manipula o evento de teclas liberadas
     * (KeyReleased) no campo de texto `txtUsuId`. Realiza verificações no valor
     * digitado e desabilita os campos da interface caso o campo esteja vazio.
     * Também chama o método `comparar()` para realizar uma lógica adicional e o
     * método `limpar()` para redefinir os campos.
     *
     * @param evt Evento acionado ao liberar uma tecla no campo de texto.
     */
    private void txtUsuIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuIdKeyReleased

        String v;

        v = txtUsuId.getText().trim();
        comparar();
        if (v.equals("")) {

            txtUsoNom.setEnabled(false);
            txtUsoLog.setEnabled(false);
            txtUsuSen.setEnabled(false);
            txtNomDir.setEnabled(false);
            txtIdaUsu.setEnabled(false);
            cboUsuPer.setEnabled(false);
            cboUsotip.setEnabled(false);
            cboSexUsu.setEnabled(false);
            cboCadAreas.setEnabled(false);
            jdcDatUsu.setEnabled(false);
            btnUsuFot.setEnabled(false);
            btnArqFotUsu.setEnabled(false);
            btnUsuUpdate.setEnabled(false);
            btnUsuDelete.setEnabled(false);
            btnUsuCreate.setEnabled(false);
            limpar();
        }

    }//GEN-LAST:event_txtUsuIdKeyReleased

    /**
     * Método `cboCadAreasActionPerformed`. Manipula o evento de seleção no
     * ComboBox `cboCadAreas`. Invoca o método `cboSetor()` para atualizar o
     * ComboBox `cboUsotip` com base na área selecionada.
     *
     * @param evt Evento acionado ao interagir com o ComboBox.
     */
    private void cboCadAreasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCadAreasActionPerformed

        cboSetor();
    }//GEN-LAST:event_cboCadAreasActionPerformed

    /**
     * Método `txtUsuIdKeyTyped`. Manipula o evento de digitação de teclas
     * (KeyTyped) no campo de texto `txtUsuId`. Filtra as entradas, permitindo
     * apenas números, e descarta caracteres não permitidos.
     *
     * @param evt Evento acionado ao pressionar e liberar uma tecla no campo de
     * texto.
     */
    private void txtUsuIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuIdKeyTyped
        //filtro que só permite a inclusão de números na caixa de texto:
        String caracter = "0123456789";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtUsuIdKeyTyped

    /**
     * Método `txtUsoNomKeyTyped`. Manipula o evento de digitação de teclas
     * (KeyTyped) no campo de texto `txtUsoNom`. Filtra as entradas para
     * permitir apenas letras (maiúsculas e minúsculas) e espaços. Descarta
     * caracteres não permitidos.
     *
     * @param evt Evento acionado ao pressionar e liberar uma tecla no campo de
     * texto.
     */
    private void txtUsoNomKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsoNomKeyTyped
        // filtro que só permite a inclusão de letras na caixa de texto:
        String caracter = "AaÁáÀàBbCcÇçDdEeÉéÈèFfGgHhIiÍíÌìJjKkLlMmNnOoÓóÒòPpQqRrSsTtUuÚúÙùVvWwXxYyZz ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtUsoNomKeyTyped

    private void txtUsoLogKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsoLogKeyTyped
        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "abcdefghijklmnopqrstuvwxyz0123456789@_.";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtUsoLogKeyTyped

    /**
     * Método `txtUsoLogKeyTyped`. Manipula o evento de digitação de teclas
     * (KeyTyped) no campo de texto `txtUsoLog`. Filtra as entradas, permitindo
     * apenas letras minúsculas, números e caracteres especiais específicos (@,
     * _ e .). Descarta caracteres não permitidos.
     *
     * @param evt Evento acionado ao pressionar e liberar uma tecla no campo de
     * texto.
     */
    private void txtUsuSenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuSenKeyTyped
        //filtro que só permite a inclusão de números na caixa de texto:
        String caracter = "0123456789";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtUsuSenKeyTyped

    /**
     * Método `txtUsuOutrosKeyTyped`. Manipula o evento de digitação de teclas
     * (KeyTyped) no campo de texto `txtUsuOutros`. Filtra as entradas,
     * permitindo apenas letras (maiúsculas e minúsculas), números e caracteres
     * especiais específicos (°, _, - e espaço). Caracteres não permitidos são
     * descartados.
     *
     * @param evt Evento acionado ao pressionar e liberar uma tecla no campo de
     * texto.
     */
    private void txtUsuOutrosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuOutrosKeyTyped
        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaÁáÀàBbCcÇçDdEeÉéÈèFfGgHhIiÍíÌìJjKkLlMmNnOoÓóÒòPpQqRrSsTtUuÚúÙùVvWwXxYyZz0123456789°_- ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtUsuOutrosKeyTyped

    /**
     * Método `cboFilterActionPerformed`. Manipula o evento de seleção no
     * ComboBox `cboFilter`. Com base na opção selecionada, realiza diferentes
     * ações: - Se a seleção for "Todos", limpa o campo de pesquisa `txtPesUsu`
     * e realiza uma pesquisa inicial. - Caso contrário, chama o método
     * `pesquisar_usu()` para executar uma pesquisa personalizada.
     *
     * @param evt Evento acionado ao interagir com o ComboBox.
     */
    private void cboFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFilterActionPerformed

        String string;
        string = cboFilter.getSelectedItem().toString();
        txtPesUsu.setText("");

        if (string.equals("Todos")) {
            pesquisa_inic();
            cboFilter.setSelectedItem("Todos");
        } else {
            pesquisar_usu();
        }
    }//GEN-LAST:event_cboFilterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnAltFot;
    private javax.swing.JButton btnArqFotUsu;
    private javax.swing.JButton btnUsuCreate;
    private javax.swing.JButton btnUsuDelete;
    private javax.swing.JButton btnUsuFot;
    private javax.swing.JButton btnUsuUpdate;
    private javax.swing.JComboBox<String> cboCadAreas;
    private javax.swing.JComboBox<String> cboFilter;
    private javax.swing.JComboBox<String> cboSexUsu;
    private javax.swing.JComboBox<String> cboUsotip;
    private javax.swing.JComboBox<String> cboUsuPer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcDatUsu;
    public static javax.swing.JLabel lblFotUsu;
    private javax.swing.JTable tblPeUsu;
    private javax.swing.JTextField txtIdaUsu;
    public static javax.swing.JTextField txtNomDir;
    private javax.swing.JTextField txtPesUsu;
    private javax.swing.JTextField txtUsoLog;
    public static javax.swing.JTextField txtUsoNom;
    private javax.swing.JTextField txtUsuId;
    private javax.swing.JTextField txtUsuOutros;
    private javax.swing.JTextField txtUsuSen;
    // End of variables declaration//GEN-END:variables
}
