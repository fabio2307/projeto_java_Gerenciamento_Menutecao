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

import javax.swing.ImageIcon;
import java.sql.*;
import com.prjmanutencao.dal.ModuloConexao;
import static com.prjmanutencao.telas.TelaPesquisaros.tblOsAberta;
import static com.prjmanutencao.telas.TelaPesquisaros.tblOsFechada;
import static com.prjmanutencao.telas.Arquivos.idOs;
import static com.prjmanutencao.telas.Arquivos.idArq;
import static com.prjmanutencao.telas.Arquivos.regra;
import static com.prjmanutencao.telas.TelaChamados.tblChamados;
import static com.prjmanutencao.telas.TelaLogin.id;
import static com.prjmanutencao.telas.TelaLogin.tip;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.*;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Realiza a abertura e encerramento de ordem de serviço.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaOs1 extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DefaultStyledDocument doc, doc1;

    private ScheduledExecutorService executor;
    private boolean executorAtivo = false;

    int seg = 0;
    int min = 0;
    int hor = 0;
    int dia = 0;
    boolean estado = true;
    volatile boolean atualizar = true;
    int segu = 0;
    Arquivos arquivos;
    String format;
    String Pesq, Pesq1;
    String area;
    String selct;
    String situacaoOs;
    StringBuilder buff = new StringBuilder();

    /**
     * Construtor `TelaOs1`. Inicializa os componentes da interface gráfica e
     * realiza as configurações iniciais. Conecta ao banco de dados e prepara
     * filtros, tabelas, layouts, e outras funcionalidades essenciais para o
     * funcionamento da aplicação.
     */
    public TelaOs1() {
        initComponents();
        conexao = ModuloConexao.conector();
        filtroInicial();
        iniciar();
//        iniciarAtualizacao();

        contagem();
        contagem1();
        updateCount();
        updateCount1();

        tblSetor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        tblArquivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaFormat();
        setorFuncionario();

        DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        ZonedDateTime zdtNow = ZonedDateTime.now();
        format = data_hora.format(zdtNow);

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
//                System.out.println("in DocumentSizeFilter's insertString method");
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
     * Método `contagem`. Configura os documentos associados a diversos campos
     * de texto (`txaDescricao`), aplicando filtros de tamanho máximo em cada
     * campo. Isso garante que os usuários não possam inserir mais caracteres do
     * que o limite definido.
     */
    private void contagem() {

        doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(100));

        txaDescricao.setDocument(doc);

        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCount();
            }
        });

    }

    /**
     * Método `contagem1`. Configura os documentos associados a diversos campos
     * de texto (`txaObservacao`), aplicando filtros de tamanho máximo em cada
     * campo. Isso garante que os usuários não possam inserir mais caracteres do
     * que o limite definido.
     */
    private void contagem1() {

        doc1 = new DefaultStyledDocument();
        doc1.setDocumentFilter(new DocumentSizeFilter(300));

        txaObservacao.setDocument(doc1);

//      
        doc1.addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCount1();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCount1();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCount1();
            }
        });

    }

    /**
     * Método `updateCount`. Atualiza o texto do rótulo `lblCarDes` com a
     * quantidade de caracteres restantes para completar o limite máximo. O
     * limite máximo é definido como 100 caracteres.
     */
    private void updateCount() {
        lblCarDes.setText((100 - doc.getLength()) + " caracteres restantes");
    }

    /**
     * Método `updateCount1`. Atualiza o texto do rótulo `lblCarObs` com a
     * quantidade de caracteres restantes para completar o limite máximo. O
     * limite máximo é definido como 300 caracteres.
     */
    private void updateCount1() {
        lblCarObs.setText((300 - doc1.getLength()) + " caracteres restantes");
    }

    /**
     * Método `tabelaFormat`. Responsável por formatar e centralizar o cabeçalho
     * e as células da tabela `tblArquivos`. Configura as larguras das colunas e
     * define o alinhamento centralizado tanto no cabeçalho quanto no conteúdo.
     *
     * Trata exceções caso ocorra algum erro durante o alinhamento da tabela.
     */
    private void tabelaFormat() {

        int width3 = 90, width4 = 313;
        JTableHeader header1;

        try {

            header1 = tblArquivos.getTableHeader();
            DefaultTableCellRenderer centralizado1 = (DefaultTableCellRenderer) header1.getDefaultRenderer();
            centralizado1.setHorizontalAlignment(SwingConstants.CENTER);
            tblArquivos.getColumnModel().getColumn(0).setPreferredWidth(width3);
            tblArquivos.getColumnModel().getColumn(1).setPreferredWidth(width4);

            DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
            centerRenderer1.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < tblArquivos.getColumnCount(); i++) {
                tblArquivos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas");
        }

    }

    /**
     * Método `setorFuncionario`. Configura e formata a tabela `tblSetor` com
     * base no contexto definido pela variável `Pesq`. Centraliza o cabeçalho e
     * as células, define as larguras das colunas e ajusta a tabela conforme o
     * tipo de pesquisa. Lida com diferentes cenários: "Setor", "Funcionário" e
     * "Técnico".
     *
     * Trata exceções em caso de erro na formatação.
     */
    private void setorFuncionario() {

        int width = 90, width1 = 205, width2 = 310, width3 = 90, width4 = 200, width5 = 165, width6 = 200;
        JTableHeader header;

        try {
            if (Pesq.equals("Setor")) {
                header = tblSetor.getTableHeader();
                DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
                centralizado.setHorizontalAlignment(SwingConstants.CENTER);
                tblSetor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
                tblSetor.getColumnModel().getColumn(0).setPreferredWidth(width);
                tblSetor.getColumnModel().getColumn(1).setPreferredWidth(width1);
                tblSetor.getColumnModel().getColumn(2).setPreferredWidth(width2);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                //onde está zero pode ser alterado para a coluna desejada
                for (int i = 0; i < tblSetor.getColumnCount(); i++) {
                    tblSetor.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

                }

            }
            if (Pesq.equals("Funcionário")) {
                header = tblSetor.getTableHeader();
                DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
                centralizado.setHorizontalAlignment(SwingConstants.CENTER);
                tblSetor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                tblSetor.getColumnModel().getColumn(0).setPreferredWidth(width3);
                tblSetor.getColumnModel().getColumn(1).setPreferredWidth(width4);
                tblSetor.getColumnModel().getColumn(2).setPreferredWidth(width5);
                tblSetor.getColumnModel().getColumn(3).setPreferredWidth(width6);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                // onde está zero pode ser alterado para a coluna desejada
                for (int i = 0; i < tblSetor.getColumnCount(); i++) {
                    tblSetor.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

                }
            }
            if (Pesq.equals("Técnico")) {
                header = tblSetor.getTableHeader();
                DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
                centralizado.setHorizontalAlignment(SwingConstants.CENTER);
                tblSetor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                tblSetor.getColumnModel().getColumn(0).setPreferredWidth(width3);
                tblSetor.getColumnModel().getColumn(1).setPreferredWidth(width4);
                tblSetor.getColumnModel().getColumn(2).setPreferredWidth(width5);
                tblSetor.getColumnModel().getColumn(3).setPreferredWidth(width6);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                //onde está zero pode ser alterado para a coluna desejada
                for (int i = 0; i < tblSetor.getColumnCount(); i++) {
                    tblSetor.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    dimensao();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas");
        }

    }

    /**
     * Método `iniciar`. Responsável por configurar a interface inicial com base
     * nas informações do banco de dados. Recupera a área de atuação do usuário
     * a partir da tabela `usuarios`, usando o identificador `id`, e define o
     * valor selecionado no ComboBox `cboSetores`. Invoca o método
     * `selecSetor()` para realizar configurações adicionais relacionadas ao
     * setor.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void iniciar() {

        String sql = "select area_usuario from usuarios where iduser = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                cboSetores.setSelectedItem(rs.getString(1));
                selecSetor();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `filtroInicial`. Configura os filtros e componentes da interface
     * gráfica com base no tipo de usuário (`tip`). Realiza ajustes específicos
     * para os usuários classificados como "Superadmin", "Admin" e "User", além
     * de configurar a interface inicial conforme as permissões e
     * funcionalidades disponíveis para cada tipo.
     *
     * Invoca métodos auxiliares e executa consultas ao banco de dados para
     * recuperar informações relacionadas ao usuário.
     *
     * @throws HeadlessException Em caso de erro no ambiente gráfico.
     * @throws SQLException Em caso de falha na execução da consulta SQL.
     */
    private void filtroInicial() {

        try {
            if (tip.equals("Superadmin")) {
                funcionariosIni();
                txtPesSet.setEditable(false);
                rbtPesFuncionario.setSelected(true);
                rbtPesFuncionario.setForeground(new Color(39, 44, 182));
                rbtPesFuncionario.setEnabled(true);

            }
            if (tip.equals("Admin")) {
                rbtPesSetor.setSelected(true);
                rbtPesFuncionario.setEnabled(true);
                pesquisarSetorIni();
            }
            if (tip.equals("User")) {
                txtPesSet.setEditable(true);
                rbtPesFuncionario.setVisible(false);
                rbtPesSetor.setSelected(true);
                rbtPesSetor.setEnabled(true);
                rbtPesTecnico.setEnabled(true);
                rbtPesSetor.setForeground(new Color(39, 44, 182));
                cboFilter.setVisible(false);
                btnEncOs.setVisible(true);
                btnEncOs.setVisible(true);
                btnImpOs.setEnabled(false);
                pesquisarSetorIni();

                String sql = "select iduser as 'R.E', nome_usuario as 'Nome', area_usuario as 'Área', tipo_usuario as 'Cargo' from usuarios where iduser = ? ";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        txtIdFun.setText(rs.getString(1));
                        txtNomFun.setText(rs.getString(2));
                        txtAreFun.setText(rs.getString(3));
                        txtTipFun.setText(rs.getString(4));
                    }
                    setorFuncionario();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `iniciarOs`. Responsável por criar e inicializar uma Ordem de
     * Serviço (OS) no banco de dados. Preenche os campos obrigatórios, valida a
     * entrada do usuário, insere dados no banco e configura a interface para o
     * estado apropriado após a abertura da OS.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void iniciarOs() {
        String sit_Os = "Aberta";
        String comp1;

        estado = true;
        regra = sit_Os;
        txtInicio.setText(format);

        String sql = "insert into os (hora_inicial, iduser1, id_setor, iduser, tipo_os, servico_os, prioridade_os, desc_os, observ_os, situacao_os, area_usuario)values (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtInicio.getText());
            pst.setString(2, txtIdFun.getText());
            pst.setString(3, txtIdSet.getText());
            pst.setString(4, txtIdTec.getText());
            pst.setString(5, cboServico.getSelectedItem().toString());
            pst.setString(6, cboSeguimento.getSelectedItem().toString());
            pst.setString(7, cboPriOs.getSelectedItem().toString());
            pst.setString(8, txaDescricao.getText());
            pst.setString(9, txaObservacao.getText());
            pst.setString(10, sit_Os);
            pst.setString(11, area);
            comp1 = txtIdTec.getText();
            if ((txtInicio.getText().isEmpty()) || (txtIdFun.getText().isEmpty()) || (txtNomFun.getText().isEmpty()) || (txtIdSet.getText().isEmpty()) || (txtSetCham.getText().isEmpty()) || (txtIdTec.getText().isEmpty()) || (txtNomTec.getText().isEmpty()) || (cboServico.getSelectedItem().toString().isEmpty()) || txaDescricao.getText().isEmpty()) {
                txtInicio.setText(null);
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios");
                txaDescricao.requestFocus();

            } else {
                int adicionar = pst.executeUpdate();
                if (adicionar > 0) {
                    if (id.equals(comp1)) {
                        id_os();
                        cronometro();
                        parametros();
                        btnIniOs.setEnabled(false);
                        btnEncOs.setEnabled(true);
                        btnNovOs.setEnabled(true);
                        btnAdcArqOs.setEnabled(true);
                        txaDescricao.setEditable(false);
                        txtPesSet.setEditable(false);
                        cboFilter.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "Ordem de Serviço Aberta com Sucesso");
                        cboSetores.setEnabled(false);
                        situacaoOs = "Aberta";
                        conexao.close();
                    } else {
                        id_os();
                        cronometro();
                        parametros();
                        btnIniOs.setEnabled(false);
                        btnEncOs.setEnabled(false);
                        btnNovOs.setEnabled(true);
                        btnAdcArqOs.setEnabled(true);
                        txaDescricao.setEditable(false);
                        txtPesSet.setEditable(false);
                        cboFilter.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "Ordem de Serviço Aberta com Sucesso");
                        cboSetores.setEnabled(false);
                        situacaoOs = "Aberta";
                        conexao.close();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Iníciar a Ordem de Serviço");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `fecharOs`. Responsável por finalizar uma Ordem de Serviço (OS) no
     * banco de dados. Atualiza os campos da OS com as informações de
     * encerramento, como observações, situação, hora final e cronômetro. Valida
     * os campos obrigatórios antes de executar a operação.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void fecharOs() {
        regra = null;
        String situacao = "Encerrada";
        txtTermino.setText(format);

        String sql = "update os set observ_os = ?, situacao_os = ?, hora_final = ?, cro_os = ? where id_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txaObservacao.getText());
            pst.setString(2, situacao);
            pst.setString(3, txtTermino.getText());
            pst.setString(4, txtTempo.getText());
            pst.setString(5, txtNumOs.getText());
            estado = false;

            if (txtTempo.getText().isEmpty() || txtTermino.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
            } else {
                int adicionar = pst.executeUpdate();
                if (adicionar > 0) {
                    btnIniOs.setEnabled(false);
                    btnEncOs.setEnabled(false);
                    btnNovOs.setEnabled(false);
                    btnAdcArqOs.setEnabled(false);
                    btnImpOs.setEnabled(true);
                    txaObservacao.setEditable(false);
                    regra = situacao;
                    JOptionPane.showMessageDialog(null, "Ordem de Serviço Encerrada!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Finalizar a Ordem de Serviço");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `id_os`. Recupera o identificador máximo da tabela `os` para
     * determinar o ID da OS mais recente. Preenche o campo `txtNumOs` com o ID
     * retornado pela consulta.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void id_os() {
        String sql = "select max(id_os) from os ";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNumOs.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `funcionariosIni`. Responsável por inicializar a exibição dos
     * funcionários com status "on-line" na tabela `tblSetor`. Define parâmetros
     * visuais e realiza consulta ao banco de dados para preencher a tabela.
     *
     * Invoca o método auxiliar `setorFuncionario()` para ajustar o layout da
     * tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void funcionariosIni() {

        Pesq = "Funcionário";
        lblMulti.setLocation(214, 185);
        lblMulti.setSize(124, 28);
        lblMulti.setText(Pesq);
        String status = "on-line";
        String sql = "select iduser as 'R.E', nome_usuario as 'Nome', area_usuario as 'Área', tipo_usuario as 'Cargo' from usuarios where status_usuario = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, status);
            rs = pst.executeQuery();
            tblSetor.setModel(DbUtils.resultSetToTableModel(rs));
            setorFuncionario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `funcionarios`. Realiza a busca de funcionários no banco de dados
     * com base nos filtros fornecidos pelo usuário. Os critérios incluem área
     * do usuário (`area_usuario`), identificador (`iduser`) ou nome
     * (`nome_usuario`), e o status "on-line". Atualiza a tabela `tblSetor` com
     * os resultados retornados.
     *
     * Invoca o método `setorFuncionario()` para ajustar o layout da tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void funcionarios() {

        String status = "on-line";
        String sql = "select iduser as 'R.E', nome_usuario as 'Nome', area_usuario as 'Área', tipo_usuario as 'Cargo' from usuarios where area_usuario like ? and iduser like ? and status_usuario = ? or area_usuario like ? and nome_usuario like ? and status_usuario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboFilter.getSelectedItem().toString() + "%");
            pst.setString(2, txtPesSet.getText() + "%");
            pst.setString(3, status);
            pst.setString(4, cboFilter.getSelectedItem().toString() + "%");
            pst.setString(5, txtPesSet.getText() + "%");
            pst.setString(6, status);
            rs = pst.executeQuery();
            tblSetor.setModel(DbUtils.resultSetToTableModel(rs));
            setorFuncionario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setarFuncionario`. Manipula os dados da tabela `tblSetor` ao
     * selecionar uma linha. Preenche os campos de texto da interface gráfica
     * (`txtIdFun`, `txtNomFun`, `txtAreFun`, `txtTipFun`) com as informações da
     * linha selecionada.
     */
    private void setarFuncionario() {

        int setar = tblSetor.getSelectedRow();
        txtIdFun.setText(tblSetor.getModel().getValueAt(setar, 0).toString());
        txtNomFun.setText(tblSetor.getModel().getValueAt(setar, 1).toString());
        txtAreFun.setText(tblSetor.getModel().getValueAt(setar, 2).toString());
        txtTipFun.setText(tblSetor.getModel().getValueAt(setar, 3).toString());
    }

    /**
     * Método `pesquisarSetorIni`. Responsável por configurar a exibição inicial
     * de setores na tabela `tblSetor`. Executa uma consulta ao banco de dados
     * para listar os setores e atualiza a tabela com os dados retornados.
     *
     * Invoca o método auxiliar `setorFuncionario()` para formatar o layout da
     * tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisarSetorIni() {
        Pesq = "Setor";
        lblMulti.setLocation(258, 185);
        lblMulti.setSize(80, 28);
        lblMulti.setText(Pesq);

        String sql = "select id_setor as Código, andar as Andar, setor as Setor from setor";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tblSetor.setModel(DbUtils.resultSetToTableModel(rs));
            setorFuncionario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisarSetor`. Realiza a busca de setores no banco de dados com
     * base no texto digitado pelo usuário no campo `txtPesSet`. Atualiza a
     * tabela `tblSetor` com os resultados correspondentes aos critérios de
     * pesquisa.
     *
     * Invoca o método auxiliar `setorFuncionario()` para ajustar o layout da
     * tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisarSetor() {

        String sql = "select id_setor as Código, andar as Andar, setor as Setor from setor where setor like ? or id_setor like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesSet.getText() + "%");
            pst.setString(2, txtPesSet.getText() + "%");
            rs = pst.executeQuery();
            tblSetor.setModel(DbUtils.resultSetToTableModel(rs));
            setorFuncionario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `setarSetor`. Manipula os dados da tabela `tblSetor` ao selecionar
     * uma linha. Preenche os campos de texto da interface gráfica (`txtIdSet`,
     * `txtAndSet`, `txtSetCham`) com as informações da linha selecionada e
     * invoca o método `tecnicoAleatorio()` caso o campo `txtIdTec` esteja
     * vazio.
     */
    private void setarSetor() {
        String str = txtIdTec.getText();

        int setar = tblSetor.getSelectedRow();
        txtIdSet.setText(tblSetor.getModel().getValueAt(setar, 0).toString());
        txtAndSet.setText(tblSetor.getModel().getValueAt(setar, 1).toString());
        txtSetCham.setText(tblSetor.getModel().getValueAt(setar, 2).toString());
        if (str.equals("")) {
            tecnicoAleatorio();
        }

    }

    /**
     * Método `pesquisarTecnicoIni`. Realiza uma busca inicial de técnicos no
     * banco de dados com base na área de atuação (`selct`) e status "on-line".
     * Atualiza a tabela `tblSetor` com os resultados retornados.
     *
     * Invoca o método auxiliar `setorFuncionario()` para ajustar o layout da
     * tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisarTecnicoIni() {
        Pesq = "Técnico";

        String status = "on-line";
        String sql = "select iduser as 'R.E', nome_usuario as 'Nome', area_usuario as 'Área', tipo_usuario as 'Cargo' from usuarios where area_usuario = ? and status_usuario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, selct);
            pst.setString(2, status);
            rs = pst.executeQuery();
            tblSetor.setModel(DbUtils.resultSetToTableModel(rs));
            setorFuncionario();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `tecnicoAleatorio`. Gera aleatoriamente um técnico para
     * atendimento, caso necessário. Busca os IDs de técnicos no banco de dados
     * com base na área (`selct`) e status "on-line", armazena os resultados em
     * uma lista e seleciona um técnico aleatoriamente.
     *
     * Invoca o método `pesquisarTecAleat` para atualizar os detalhes do técnico
     * selecionado na interface.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void tecnicoAleatorio() {
        ArrayList<String> tecnico = new ArrayList();

        int i;
        String status = "on-line";

        String sql = "select iduser from usuarios where area_usuario = ? and status_usuario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, selct);
            pst.setString(2, status);
            rs = pst.executeQuery();
            while (rs.next()) {
                //[ B ] usando o método add() para gravar o iduser dos tecnos para o
                //sorteio para atendimento
                tecnico.add((rs.getString(1)));
            }

            // [ C ] mostrando os "iduser" dos tecnicos (usando o índice)
            // número de elementos de tecnicos: método size()
//            System.out.printf("Percorrendo o ArrayList (usando o índice)\n");
            int n = tecnico.size();
            for (i = 0; i < n; i++) {
//                System.out.printf("Posição %d- %s\n", i, agenda.get(i));
            }
            int index = (int) (Math.random() * tecnico.size());
            txtIdTec.setText(tecnico.get(index));
            pesquisarTecAleat();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisarTecAleat`. Busca os detalhes do técnico selecionado
     * aleatoriamente com base na área (`selct`) e ID do técnico (`txtIdTec`).
     * Preenche os campos de texto (`txtNomTec`, `txtEspTec`) e define a área do
     * técnico na variável `area`.
     *
     * Invoca o método `setorFuncionario()` para ajustar o layout da tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisarTecAleat() {
        area = null;
        String status = "on-line";

        String sql = "select nome_usuario as 'Técnico', tipo_usuario as 'Cargo', area_usuario from usuarios where area_usuario = ? and iduser = ? and status_usuario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, selct);
            pst.setString(2, txtIdTec.getText());
            pst.setString(3, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNomTec.setText(rs.getString(1));
                txtEspTec.setText(rs.getString(2));
                area = (rs.getString(3));
            }
            setorFuncionario();
            cboServicoOs();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisarTecnico`. Realiza uma busca de técnicos no banco de
     * dados com base na área (`selct`) e texto inserido no campo `txtPesSet`.
     * Atualiza a tabela `tblSetor` com os técnicos que atendem aos critérios.
     *
     * Invoca o método `setorFuncionario()` para ajustar o layout da tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisarTecnico() {

        String status = "on-line";

        String sql = "select iduser as 'R.E', nome_usuario as 'Técnico', tipo_usuario as 'Cargo' from usuarios where area_usuario = ? and iduser like ? and status_usuario = ? or area_usuario = ? and nome_usuario like ? and status_usuario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, selct);
            pst.setString(2, txtPesSet.getText() + "%");
            pst.setString(3, status);
            pst.setString(4, selct);
            pst.setString(5, txtPesSet.getText() + "%");
            pst.setString(6, status);
            rs = pst.executeQuery();
            tblSetor.setModel(DbUtils.resultSetToTableModel(rs));
            setorFuncionario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setarTecnico`. Preenche os campos de texto com os dados do
     * técnico selecionado na tabela `tblSetor`. Zera o campo `txtPesSet` para
     * limpar a pesquisa anterior.
     */
    private void setarTecnico() {

        int setar = tblSetor.getSelectedRow();
        txtIdTec.setText(tblSetor.getModel().getValueAt(setar, 0).toString());
        txtNomTec.setText(tblSetor.getModel().getValueAt(setar, 1).toString());
        area = (tblSetor.getModel().getValueAt(setar, 2).toString());
        txtEspTec.setText(tblSetor.getModel().getValueAt(setar, 3).toString());

        txtPesSet.setText("");
        cboServicoOs();
    }

    /**
     * Método cboServicoOs
     *
     * Responsável por identificar automaticamente o setor técnico mencionado no
     * campo `txtEspTec` e selecionar o item correspondente no combo box
     * `cboServico`.
     *
     * Funcionalidades: - Obtém o texto digitado pelo usuário (`txtEspTec`) e
     * armazena em `buffer`. - Define os setores válidos: "Elétrica",
     * "Hidráulica", "Civil", "Refrigeração". - Utiliza `Stream` para filtrar as
     * palavras do texto que correspondem aos setores válidos. - Concatena os
     * setores encontrados em uma única `String` (sem colchetes). - Se o
     * resultado for um setor válido, seleciona-o no combo box. - Caso
     * contrário, define "Civil" como padrão. - Em caso de exceção, exibe uma
     * mensagem de erro via `JOptionPane`.
     *
     * Observações: - A lógica atual funciona corretamente apenas se houver
     * **uma única palavra válida**. Se houver múltiplas, o `builder.toString()`
     * não será igual a nenhum item da lista.
     *
     * Sugestão de melhoria: - Selecionar o **primeiro setor válido**
     * encontrado, se houver mais de um. - Exemplo: `if
     * (!palavrasFiltradas.isEmpty())
     * cboServico.setSelectedItem(palavrasFiltradas.get(0));`
     *
     * Esse método torna o preenchimento do setor mais inteligente e
     * automatizado, reduzindo erros manuais e acelerando o processo de
     * cadastro.
     */
    private void cboServicoOs() {

        StringBuilder builder = new StringBuilder();
        String buffer;
        buffer = (txtEspTec.getText());

        try {

            List<String> setoresValidos = Arrays.asList("Elétrica", "Hidráulica", "Civil", "Refrigeração");

            // Usando stream para filtrar
            List<String> palavrasFiltradas = Arrays.stream(buffer.split("\\s+")).filter(palavra -> setoresValidos.contains(palavra)).collect(Collectors.toList());
            builder.append((palavrasFiltradas.toString().replace("[", "").replace("]", "")));

            if (setoresValidos.contains(builder.toString())) {
                cboServico.setSelectedItem(builder.toString());
            } else {
                cboServico.setSelectedItem("Civil");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Ateção", 2);
        }
    }

    /**
     * Método `setOsInicial`. Seleciona uma Ordem de Serviço (OS) aberta e
     * preenche o campo `txtNumOs` com seu identificador. Invoca métodos
     * auxiliares para carregar informações adicionais e atualizar a interface.
     */
    public void setOsInicial() {
        int setar = tblOsAberta.getSelectedRow();
        txtNumOs.setText(tblOsAberta.getModel().getValueAt(setar, 0).toString());
        quantidadeArquivo();
        dadosOsInicial();
    }

    /**
     * Método `setOsInicialCham`. Seleciona um chamado de OS e preenche o campo
     * `txtNumOs` com seu identificador. Invoca métodos auxiliares para carregar
     * informações adicionais e atualizar a interface.
     */
    public void setOsInicialCham() {
        int setar = tblChamados.getSelectedRow();
        txtNumOs.setText(tblChamados.getModel().getValueAt(setar, 0).toString());
        quantidadeArquivo();
        dadosOsInicial();
    }

    /**
     * Método `dadosOsInicial`. Recupera informações detalhadas de uma Ordem de
     * Serviço (OS) do banco de dados e preenche os campos da interface gráfica
     * com os dados retornados. Realiza ajustes na interface com base nos
     * valores retornados e no estado da OS.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void dadosOsInicial() {

        String comp = null;

        String sql = "select  Os.hora_inicial, Os.situacao_os, Os.iduser1, Os.id_setor, Os.iduser, Os.tipo_os, Os.servico_os, prioridade_os, Os.desc_os, Os.observ_os, situacao_os, (select nome_usuario from usuarios where iduser = Os.iduser1), (select area_usuario from usuarios where iduser = Os.iduser1), (select tipo_usuario from usuarios where iduser = Os.iduser1), (select andar from setor where id_setor = Os.id_setor ), (select setor from setor where id_setor = Os.id_setor ), (select nome_usuario from usuarios where iduser = Os.iduser ), (select tipo_usuario from usuarios where iduser = Os.iduser ), (select tipo_usuario from usuarios where iduser = Os.iduser ) from os Os where Os.id_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumOs.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtInicio.setText(rs.getString(1));
                regra = (rs.getString(2));
                txtIdFun.setText(rs.getString(3));
                txtIdSet.setText(rs.getString(4));
                txtIdTec.setText(rs.getString(5));
                comp = (rs.getString(5));
                cboServico.setSelectedItem(rs.getString(6));
                cboSeguimento.setSelectedItem(rs.getString(7));
                cboPriOs.setSelectedItem(rs.getString(8));
                txaDescricao.setText(rs.getString(9));
                txaObservacao.setText(rs.getString(10));
                situacaoOs = (rs.getString(11));
                txtNomFun.setText(rs.getString(12));
                txtAreFun.setText(rs.getString(13));
                txtTipFun.setText(rs.getString(14));
                txtAndSet.setText(rs.getString(15));
                txtSetCham.setText(rs.getString(16));
                txtNomTec.setText(rs.getString(17));
                txtEspTec.setText(rs.getString(18));

                if (id.equals(comp)) {
                    btnEncOs.setEnabled(true);
                    parametros();
                    cauc_tempo();
                    cboSetores.setEnabled(false);
                    arquivosBuscaIni();
                } else {
                    btnEncOs.setEnabled(false);
                    parametros();
                    cauc_tempo();
                    cboSetores.setEnabled(false);
                    arquivosBuscaIni();
                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setOsFinal`. Seleciona uma Ordem de Serviço (OS) finalizada na
     * tabela `tblOsFechada` e atualiza o campo `txtNumOs` com seu
     * identificador. Invoca métodos auxiliares para carregar informações
     * adicionais e atualizar a interface.
     */
    public void setOsFinal() {
        int setar = tblOsFechada.getSelectedRow();
        txtNumOs.setText(tblOsFechada.getModel().getValueAt(setar, 0).toString());
        quantidadeArquivo();
        dadosOsFinal();
    }

    /**
     * Método `dadosOsFinal`. Este método realiza uma consulta ao banco de dados
     * para obter informações detalhadas de uma Ordem de Serviço (OS) baseada em
     * seu número (`Os.id_os`). As informações recuperadas incluem horários,
     * setor, técnico responsável, descrição, observações e outros atributos
     * relacionados. Após a consulta, os dados são exibidos em vários
     * componentes da interface gráfica, sendo que certos elementos são
     * configurados para refletir o estado final da OS.
     *
     * Funcionalidades: - **Query SQL com Subconsultas**: - Utiliza subconsultas
     * para obter dados complementares relacionados a usuários e setores: -
     * Informações do solicitante (`nome_usuario`, `area_usuario`,
     * `tipo_usuario`). - Dados do setor (`andar`, `setor`). - Dados do técnico
     * responsável (`nome_usuario`, `area_usuario`, `tipo_usuario`). - Recupera
     * diversos atributos da OS, como horários, prioridade, descrição e
     * observações.
     *
     * - **Preenchimento da Interface**: - Atualiza diversos campos e
     * componentes da interface gráfica com os dados retornados pela consulta. -
     * Configura comboboxes (`cboServico`, `cboSeguimento`, `cboPriOs`) e campos
     * de texto (`txtInicio`, `txtTermino`, etc.).
     *
     * - **Definição de Regras**: - Atribui o valor do status (`situacao_os`) à
     * variável `regra`, que pode influenciar o comportamento da interface.
     *
     * - **Configuração de Componentes**: - Desativa certos componentes, como
     * `cboSetores` e `rbtPesFuncionario`, para restringir a interação do
     * usuário. - Invoca métodos auxiliares como `arquivosBuscaIni()` e
     * `parametros()` para completar a configuração inicial.
     *
     * - **Tratamento de Exceções**: - Captura possíveis falhas na consulta SQL
     * e exibe uma mensagem amigável ao usuário.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL com base no número
     * da OS (`txtNumOs.getText()`). 2. Caso haja resultados, preenche os campos
     * e componentes da interface com os dados obtidos. 3. Desativa elementos da
     * interface conforme necessário e invoca métodos auxiliares para concluir a
     * configuração. 4. Trata possíveis erros e exibe mensagens informativas ao
     * usuário.
     */
    private void dadosOsFinal() {

        String sql = "select  Os.hora_inicial, Os.hora_final, Os.cro_os, Os.situacao_os, Os.iduser1, Os.id_setor, Os.iduser, Os.tipo_os, Os.servico_os, prioridade_os, Os.desc_os, Os.observ_os, Os.situacao_os, (select nome_usuario from usuarios where iduser = Os.iduser1), (select area_usuario from usuarios where iduser = Os.iduser1), (select tipo_usuario from usuarios where iduser = Os.iduser1), (select andar from setor where id_setor = Os.id_setor ), (select setor from setor where id_setor = Os.id_setor ), (select nome_usuario from usuarios where iduser = Os.iduser ), (select area_usuario from usuarios where iduser = Os.iduser ), (select tipo_usuario from usuarios where iduser = Os.iduser ) from os Os where Os.id_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumOs.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtInicio.setText(rs.getString(1));
                txtTermino.setText(rs.getString(2));
                txtTempo.setText(rs.getString(3));
                regra = (rs.getString(4));
                txtIdFun.setText(rs.getString(5));
                txtIdSet.setText(rs.getString(6));
                txtIdTec.setText(rs.getString(7));
                cboServico.setSelectedItem(rs.getString(8));
                cboSeguimento.setSelectedItem(rs.getString(9));
                cboPriOs.setSelectedItem(rs.getString(10));
                txaDescricao.setText(rs.getString(11));
                txaObservacao.setText(rs.getString(12));
                situacaoOs = (rs.getString(13));
                txtNomFun.setText(rs.getString(14));
                txtAreFun.setText(rs.getString(15));
                txtTipFun.setText(rs.getString(16));
                txtAndSet.setText(rs.getString(17));
                txtSetCham.setText(rs.getString(18));
                txtNomTec.setText(rs.getString(19));
                txtEspTec.setText(rs.getString(20));

                arquivosBuscaIni();
                parametros();
                cboSetores.setEnabled(false);
                rbtPesFuncionario.setEnabled(false);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `parametros`. Ajusta a interface gráfica e as configurações com
     * base na situação da Ordem de Serviço (OS), indicada pela variável
     * `regra`. Define estados dos botões, tabelas e componentes da interface
     * conforme a OS esteja "Encerrada" ou em outro estado (ativo).
     */
    private void parametros() {

        if (regra.equals("Encerrada")) {
            filtroInicial();
            btnIniOs.setEnabled(false);
            btnEncOs.setEnabled(false);
            btnNovOs.setEnabled(false);
            btnAdcArqOs.setEnabled(false);
            btnImpOs.setEnabled(true);
            txaDescricao.setEditable(false);
            txaObservacao.setEditable(false);
            cboPriOs.setEnabled(false);
            cboFilter.setEnabled(false);
            cboSeguimento.setEnabled(false);
            cboServico.setEnabled(false);
            txtPesSet.setEditable(false);
            rbtPesSetor.setEnabled(false);
            rbtPesTecnico.setEnabled(false);
            ((DefaultTableModel) tblSetor.getModel()).setRowCount(0);
            tblArquivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        } else {
            filtroInicial();
            btnIniOs.setEnabled(false);
            btnNovOs.setEnabled(true);
            btnAdcArqOs.setEnabled(true);
            btnImpOs.setEnabled(false);
            cboPriOs.setEnabled(false);
            cboFilter.setEnabled(false);
            cboSeguimento.setEnabled(false);
            cboServico.setEnabled(false);
            txtPesSet.setEditable(false);
            txaDescricao.setEditable(false);
            rbtPesFuncionario.setEnabled(false);
            rbtPesFuncionario.setForeground(new Color(39, 44, 182));
            rbtPesSetor.setEnabled(false);
            rbtPesSetor.setForeground(new Color(0, 0, 50));
            rbtPesTecnico.setEnabled(false);
            rbtPesTecnico.setForeground(new Color(0, 0, 50));
            ((DefaultTableModel) tblSetor.getModel()).setRowCount(0);
            tblArquivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        }

    }

    /**
     * Método `cronometro`. Inicia um cronômetro que é executado em uma thread
     * separada, atualizando continuamente o tempo decorrido no campo de texto
     * `txtTempo`. O cronômetro para quando a variável `estado` é definida como
     * `false`. O tempo é exibido no formato "dias - horas : minutos :
     * segundos", com preenchimento de zeros à esquerda para valores menores que
     * 10.
     */
    private void cronometro() {

        Thread cron = new Thread() {
            @Override
            public void run() {

                for (;;) {
                    if (estado == true) {
                        try {
                            sleep(1000);
                            if (seg >= 60) {
                                seg = 0;
                                min++;
                            }
                            if (min >= 60) {
                                seg = 0;
                                min = 0;
                                hor++;
                            }
                            if (hor >= 24) {
                                min = 0;
                                hor = 0;
                                dia++;
                            }
                            if (dia >= 90000) {
                                hor = 0;
                                dia = 0;
                            }

                            txtTempo.setText((dia <= 9 ? "0" : "") + dia + " - " + (hor <= 9 ? "0" : "") + hor + " : " + (min <= 9 ? "0" : "") + min + " : " + (seg <= 9 ? "0" : "") + seg);

                            seg++;
                        } catch (InterruptedException e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    } else {
                        break;
                    }
                }

            }
        };

        cron.start();
    }

    /**
     * Método `cauc_tempo`. Calcula a diferença entre a data/hora inicial
     * (`txtInicio`) e a data/hora atual. O resultado é convertido em dias,
     * horas, minutos e segundos, que são armazenados em variáveis globais
     * (`seg`, `min`, `hor`, `dia`) e exibidos no cronômetro através do método
     * `cronometro()`.
     *
     * Trata erros de parsing e exibe mensagens amigáveis ao usuário em caso de
     * falha.
     *
     * @throws ParseException Em caso de erro ao converter as datas.
     */
    private void cauc_tempo() {

        estado = true;
        String segud;
        String minu;
        String hora;
        String dia1;

        String temp;
        String temp1;
        String temp2;
        String temp3;

        DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        ZonedDateTime zdtNow = ZonedDateTime.now();
        String format = data_hora.format(zdtNow);

        String dateStart = txtInicio.getText();
        String dateStop = format;

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat formate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        java.util.Date d1;
        java.util.Date d2;

        try {
            d1 = formate.parse(dateStart);
            d2 = formate.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            dia1 = ((diffDays <= 9 ? "0" : "") + diffDays);
            hora = ((diffHours <= 9 ? "0" : "") + diffHours);
            minu = ((diffMinutes <= 9 ? "0" : "") + diffMinutes);
            segud = ((diffSeconds <= 9 ? "0" : "") + diffSeconds);

            temp = segud;
            temp1 = minu;
            temp2 = hora;
            temp3 = dia1;
            seg = Integer.valueOf(temp);
            min = Integer.valueOf(temp1);
            hor = Integer.valueOf(temp2);
            dia = Integer.valueOf(temp3);
            cronometro();

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(rootPane, "Ocorreu um erro ao calcular o Tempo de Ordem de Serviço!" + "\n" + ex, "Atenção", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Método `iniciarAtualizacao`. Responsável por iniciar um executor agendado
     * para realizar atualizações automáticas. Este executor verifica o estado
     * e, se necessário, cria um novo para executar os métodos
     * `arquivosBuscaIni()` e `quantidadeArquivo()` em intervalos regulares.
     * Além disso, exibe no console o status da atualização. Em caso de
     * exceções, exibe uma mensagem de erro ao usuário.
     */
    private void iniciarAtualizacao() {

        try {

            if (executor == null || executor.isShutdown() || executor.isTerminated()) {
                executor = Executors.newScheduledThreadPool(1); // Cria um novo executor
                executor.scheduleAtFixedRate(() -> {

                    //Chamada ao módulo arquivosBuscaIni()
                    arquivosBuscaIni();
                    // Chamada ao módulo quantidadeArquivo()
                    quantidadeArquivo();
                }, 0, 5, TimeUnit.SECONDS);
                executorAtivo = true;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro durante a atualização automática: " + "\n" + e);
        }
    }

    /**
     * Método `pararAtualizacao`. Responsável por parar o executor agendado
     * utilizado para atualizações automáticas. Verifica o estado do executor
     * antes de desligá-lo para evitar exceções. Em caso de erro, exibe uma
     * mensagem de erro para o usuário.
     */
    private void pararAtualizacao() {

        try {

            if (executor != null && !executor.isShutdown() && executorAtivo) {
                executor.shutdown();
                executorAtivo = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `quantidadeArquivo`. Calcula e exibe a quantidade total de
     * arquivos associados a uma Ordem de Serviço (OS) específica. Executa uma
     * consulta ao banco de dados para contar os registros na tabela
     * `arquivos_os` relacionados ao ID da OS.
     *
     * Preenche o campo de texto `txtQuaArq` com o resultado da consulta.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void quantidadeArquivo() {

        String sql = "select count(*) from arquivos_os  where id_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumOs.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaArq.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            pararAtualizacao();
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `quantidadeArquivo`. Calcula e exibe a quantidade total de
     * arquivos associados a uma Ordem de Serviço (OS) específica. Executa uma
     * consulta ao banco de dados para contar os registros na tabela
     * `arquivos_os` relacionados ao ID da OS.
     *
     * Preenche o campo de texto `txtQuaArq` com o resultado da consulta.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void arquivosBuscaIni() {

        String sql = "select id_arq as 'N° Arquivo', nome_arq as 'Nome' from arquivos_os where id_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumOs.getText());
            rs = pst.executeQuery();
            tblArquivos.setModel(DbUtils.resultSetToTableModel(rs));
            VerificaTabela();
            tabelaFormat();
        } catch (SQLException e) {
            pararAtualizacao();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `arquivosBusca`. Realiza a busca de arquivos associados a uma
     * Ordem de Serviço (OS) no banco de dados. A pesquisa é baseada no ID da OS
     * (`id_os`) e no texto digitado no campo `txtPesArq`. Atualiza a tabela
     * `tblArquivos` com os resultados da pesquisa.
     *
     * Invoca o método `tabelaFormat()` para ajustar o layout e o formato da
     * tabela.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void arquivosBusca() {

        String sql = "select id_arq as 'N° Arquivo', nome_arq as 'Nome' from arquivos_os where id_os = ? and  id_arq like ? or id_os = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumOs.getText());
            pst.setString(2, txtPesArq.getText() + "%");
            pst.setString(3, txtNumOs.getText());
            pst.setString(4, txtPesArq.getText() + "%");
            rs = pst.executeQuery();
            tblArquivos.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `setArquivoOs`. Responsável por capturar o arquivo selecionado na
     * tabela `tblArquivos` e associá-lo à Ordem de Serviço (OS) atual. Atualiza
     * as variáveis globais `idArq` (ID do arquivo) e `idOs` (ID da OS) com base
     * na seleção feita na tabela.
     */
    private void setArquivoOs() {
        int setar = tblArquivos.getSelectedRow();
        idArq = (tblArquivos.getModel().getValueAt(setar, 0).toString());
        idOs = txtNumOs.getText();
    }

    /**
     * Método `limpar`. Restaura o estado inicial da interface gráfica ao
     * redefinir variáveis, limpar campos de texto e reconfigurar botões,
     * tabelas e outros componentes visuais da aplicação. É usado para preparar
     * a interface para uma nova operação ou reinicializar os valores após uma
     * interação.
     */
    private void limpar() {

        estado = false;
        atualizar = true;
        seg = 0;
        min = 0;
        hor = 0;
        dia = 0;

        txtTempo.setText(null);
        txtPesSet.setEditable(true);
        txtNumOs.setText("");
        txtInicio.setText("");
        txtTermino.setText("");
        txtTempo.setText("00 - 00 : 00 : 00");
        txtIdFun.setText("");
        txtNomFun.setText("");
        txtAreFun.setText("");
        txtTipFun.setText("");
        txtIdTec.setText("");
        txtNomTec.setText("");
        txtEspTec.setText("");
        txtIdSet.setText("");
        txtAndSet.setText("");
        txtSetCham.setText("");
        txtQuaArq.setText("0");
        txaDescricao.setText("");
        txaObservacao.setText("");
        txaDescricao.setEditable(true);
        txaObservacao.setEditable(true);
        cboFilter.setSelectedItem("Todos");
        cboFilter.setEnabled(true);
        cboPriOs.setSelectedItem("Baixa");
        cboSetores.setEnabled(true);
        cboPriOs.setEnabled(true);
        cboSeguimento.setEnabled(true);
        cboServico.setEnabled(true);
        btnIniOs.setEnabled(true);
        btnEncOs.setEnabled(false);
        btnNovOs.setEnabled(false);
        btnAdcArqOs.setEnabled(false);
        btnImpOs.setEnabled(false);
        rbtPesFuncionario.setEnabled(true);
        rbtPesFuncionario.setForeground(new Color(39, 44, 182));
        rbtPesSetor.setEnabled(true);
        rbtPesTecnico.setEnabled(true);
        ((DefaultTableModel) tblArquivos.getModel()).setRowCount(0);
        contagem();
        contagem1();
        updateCount();
        filtroInicial();

    }

    /**
     * Método `VerificaTabela`. Verifica se há linhas na tabela `tblArquivos` e
     * ajusta a capacidade de edição do campo de texto `txtPesArq` com base no
     * resultado. Se a tabela estiver vazia, desabilita o campo de texto; caso
     * contrário, habilita-o.
     */
    private void VerificaTabela() {
        Integer rows = tblArquivos.getModel().getRowCount();
        if (rows <= 0) {
            txtPesArq.setEditable(false);
        } else {
            txtPesArq.setEditable(true);
        }
    }

    /**
     * Método `dimensao`. Ajusta a posição, tamanho e texto do rótulo `lblMulti`
     * com base na área selecionada na variável `selct`. Cada área definida em
     * `selct` possui configurações específicas, permitindo personalização para
     * diferentes setores. Caso o valor de `selct` não corresponda às áreas
     * listadas, uma configuração padrão é aplicada.
     */
    private void dimensao() {

        if (selct.equals("A e B (Alimentos e Bebidas)")) {
            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        }
        if (selct.equals("Administrativo")) {
            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        }
        if (selct.equals("Eventos")) {
            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        }
        if (selct.equals("Governança")) {

            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        }
        if (selct.equals("Manutenção")) {

            lblMulti.setLocation(248, 185);
            lblMulti.setSize(90, 28);
            lblMulti.setText(Pesq1);
        }
        if (selct.equals("Mordomia")) {

            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        }
        if (selct.equals("Recepção")) {
            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        }
        if (selct.equals("Segurança")) {
            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        } else {
            lblMulti.setLocation(209, 185);
            lblMulti.setSize(130, 28);
            lblMulti.setText(Pesq1);
        }

    }

    /**
     * Método `selecSetor`. Configura a interface e os componentes com base na
     * área selecionada (`selct`). Cada caso no `switch` ajusta elementos
     * visuais, como botões, ComboBoxes e campos de texto, para refletir os
     * requisitos específicos do setor escolhido.
     */
    private void selecSetor() {

        switch (selct) {

            case "A e B (Alimentos e Bebidas)":

                Pesq1 = "Colaborador";
                lblNomeCol.setText("Atendido pelo Colaborador");
                rbtPesTecnico.setText("Colaborador");
                btnIniOs.setEnabled(false);
                btnNovOs.setEnabled(false);
                btnAdcArqOs.setEnabled(false);
                btnImpOs.setEnabled(false);
                cboFilter.setEnabled(false);
                lblSeguimento.setVisible(false);
                cboSeguimento.setVisible(false);
                lblServico.setVisible(false);
                cboServico.setVisible(false);
                cboPriOs.setVisible(false);
                lblPrioridade.setVisible(false);
                txtPesSet.setEditable(false);
                txaDescricao.setEditable(false);
                txaObservacao.setEditable(false);
                rbtPesFuncionario.setEnabled(false);
                rbtPesFuncionario.setForeground(new Color(39, 44, 182));
                rbtPesSetor.setEnabled(false);
                rbtPesSetor.setForeground(new Color(0, 0, 50));
                rbtPesTecnico.setEnabled(false);
                rbtPesTecnico.setForeground(new Color(0, 0, 50));
                ((DefaultTableModel) tblSetor.getModel()).setRowCount(0);

                break;

            case "Administrativo":
                Pesq1 = "Colaborador";
                lblNomeCol.setText("Atendido pelo Colaborador");
                rbtPesTecnico.setText("Colaborador");
                btnIniOs.setEnabled(false);
                btnNovOs.setEnabled(false);
                btnAdcArqOs.setEnabled(false);
                btnImpOs.setEnabled(false);
                cboFilter.setEnabled(false);
                lblSeguimento.setVisible(false);
                cboSeguimento.setVisible(false);
                lblServico.setVisible(false);
                cboServico.setVisible(false);
                cboPriOs.setVisible(false);
                lblPrioridade.setVisible(false);
                txtPesSet.setEditable(false);
                txaDescricao.setEditable(false);
                txaObservacao.setEditable(false);
                rbtPesFuncionario.setEnabled(false);
                rbtPesFuncionario.setForeground(new Color(39, 44, 182));
                rbtPesSetor.setEnabled(false);
                rbtPesSetor.setForeground(new Color(0, 0, 50));
                rbtPesTecnico.setEnabled(false);
                rbtPesTecnico.setForeground(new Color(0, 0, 50));
                ((DefaultTableModel) tblSetor.getModel()).setRowCount(0);

                break;

            case "Eventos":
                Pesq1 = "Colaborador";
                rbtPesTecnico.setText("Colaborador");
                lblNomeCol.setText("Atendido pelo Colaborador");
                lblSeguimento.setVisible(false);
                cboSeguimento.setVisible(false);
                lblServico.setVisible(false);
                cboServico.setVisible(false);
                lblPri.setVisible(false);
                cboPriOs.setVisible(false);
                lblPrioridade.setVisible(false);

                break;
            case "Governança":

                cboSeguimento.setModel(new DefaultComboBoxModel());
                String cbo1[] = {"Arrumação", "Limpeza", "Saída", "Inspeção"};
                for (String string : cbo1) {
                    cboSeguimento.addItem(string);
                }

                Pesq1 = "Colaborador";
                rbtPesTecnico.setText("Colaborador");
                lblNomeCol.setText("Atendido pelo Colaborador");
                lblSeguimento.setVisible(true);
                cboSeguimento.setVisible(true);
                lblServico.setVisible(false);
                cboServico.setVisible(false);
                lblPri.setVisible(false);
                cboPriOs.setVisible(true);
                lblPrioridade.setVisible(true);
                break;
            case "Manutenção":
                Pesq1 = "Técnico";

                cboSeguimento.setModel(new DefaultComboBoxModel());
                String cbo2[] = {"Preventiva", "Corretiva", "Proativa"};
                for (String string : cbo2) {
                    cboSeguimento.addItem(string);
                }

                lblNomeCol.setText("Atendido pelo Técnico");
                rbtPesTecnico.setText("Técnico");
                lblSeguimento.setVisible(true);
                cboSeguimento.setVisible(true);
                lblServico.setVisible(true);
                cboServico.setVisible(true);
                lblPri.setVisible(true);
                cboPriOs.setVisible(true);
                lblPrioridade.setVisible(true);

                break;
            case "Mordomia":
                Pesq1 = "Colaborador";
                lblNomeCol.setText("Atendido pelo Colaborador");
                rbtPesTecnico.setText("Colaborador");
                lblSeguimento.setVisible(false);
                cboSeguimento.setVisible(false);
                lblServico.setVisible(false);
                cboServico.setVisible(false);
                lblPri.setVisible(false);
                cboPriOs.setVisible(false);
                lblPrioridade.setVisible(false);

                break;
            case "Recepção":
                Pesq1 = "Colaborador";
                lblNomeCol.setText("Atendido pelo Colaborador");
                rbtPesTecnico.setText("Colaborador");
                btnIniOs.setEnabled(false);
                btnNovOs.setEnabled(false);
                btnAdcArqOs.setEnabled(false);
                btnImpOs.setEnabled(false);
                cboFilter.setEnabled(false);
                lblSeguimento.setVisible(false);
                cboSeguimento.setVisible(false);
                lblServico.setVisible(false);
                cboServico.setVisible(false);
                cboPriOs.setVisible(false);
                lblPrioridade.setVisible(false);
                txtPesSet.setEditable(false);
                txaDescricao.setEditable(false);
                txaObservacao.setEditable(false);
                rbtPesFuncionario.setEnabled(false);
                rbtPesFuncionario.setForeground(new Color(39, 44, 182));
                rbtPesSetor.setEnabled(false);
                rbtPesSetor.setForeground(new Color(0, 0, 50));
                rbtPesTecnico.setEnabled(false);
                rbtPesTecnico.setForeground(new Color(0, 0, 50));
                ((DefaultTableModel) tblSetor.getModel()).setRowCount(0);

                break;

            case "Segurança":
                Pesq1 = "Colaborador";
                lblNomeCol.setText("Atendido pelo Colaborador");
                rbtPesTecnico.setText("Colaborador");
                btnIniOs.setEnabled(false);
                btnNovOs.setEnabled(false);
                btnAdcArqOs.setEnabled(false);
                btnImpOs.setEnabled(false);
                cboFilter.setEnabled(false);
                lblSeguimento.setVisible(false);
                cboSeguimento.setVisible(false);
                lblServico.setVisible(false);
                cboServico.setVisible(false);
                cboPriOs.setVisible(false);
                lblPrioridade.setVisible(false);
                txtPesSet.setEditable(false);
                txaDescricao.setEditable(false);
                txaObservacao.setEditable(false);
                rbtPesFuncionario.setEnabled(false);
                rbtPesFuncionario.setForeground(new Color(39, 44, 182));
                rbtPesSetor.setEnabled(false);
                rbtPesSetor.setForeground(new Color(0, 0, 50));
                rbtPesTecnico.setEnabled(false);
                rbtPesTecnico.setForeground(new Color(0, 0, 50));
                ((DefaultTableModel) tblSetor.getModel()).setRowCount(0);

                break;
            default:

        }
    }

    /**
     * Método `imprimir`. Responsável por gerar e visualizar um relatório usando
     * o JasperReports com base no ID da Ordem de Serviço (OS). Confirma com o
     * usuário antes de iniciar a impressão e configura a visualização do
     * relatório.
     *
     * Caso ocorra um erro (como `NumberFormatException` ou `JRException`),
     * exibe uma mensagem ao usuário.
     *
     * @throws NumberFormatException Em caso de erro na conversão do ID da OS.
     * @throws JRException Em caso de falha ao gerar o relatório.
     */
    private void imprimir() {

        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a Impressão?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {

                InputStream caminhoRelativo = getClass().getResourceAsStream("/imprimir/ImprimirOs.jasper");
                URL diretorioBase = getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png");

                // Adiciona os parâmetros
                Map<String, Object> parameters = new HashMap<>();

                JasperPrint print;
                JasperViewer viewer;
                Image icone;

                // Define o diretório base para os recursos do relatório
                parameters.put("REPORT_DIR1", diretorioBase); // Caminho relativo configurado

                parameters.put("idOs", Integer.parseInt(txtNumOs.getText()));

                print = JasperFillManager.fillReport(caminhoRelativo, parameters, conexao);
                viewer = new JasperViewer(print, false);
                // Define o título da janela
                viewer.setTitle("Ordem de Serviço");
                icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));
                viewer.setIconImage(icone);
                viewer.setVisible(true);

            } catch (NumberFormatException | JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        btgPesquisa = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPesSet = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lblMulti = new javax.swing.JLabel();
        txtTempo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtInicio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTermino = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSetor = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtNumOs = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtIdSet = new javax.swing.JTextField();
        txtSetCham = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNomFun = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtIdFun = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        lblPri = new javax.swing.JLabel();
        cboServico = new javax.swing.JComboBox<>();
        lblServico = new javax.swing.JLabel();
        cboSeguimento = new javax.swing.JComboBox<>();
        txtIdTec = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaObservacao = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaDescricao = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblArquivos = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        lblCarObs = new javax.swing.JLabel();
        lblCarDes = new javax.swing.JLabel();
        txtPesArq = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtQuaArq = new javax.swing.JTextField();
        btnIniOs = new javax.swing.JButton();
        btnEncOs = new javax.swing.JButton();
        btnAdcArqOs = new javax.swing.JButton();
        btnImpOs = new javax.swing.JButton();
        txtNomTec = new javax.swing.JTextField();
        btnNovOs = new javax.swing.JButton();
        txtAndSet = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtEspTec = new javax.swing.JTextField();
        lblNomeCol = new javax.swing.JLabel();
        txtAreFun = new javax.swing.JTextField();
        lblSeguimento = new javax.swing.JLabel();
        txtTipFun = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        cboFilter = new javax.swing.JComboBox<>();
        cboPriOs = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        lblPrioridade = new javax.swing.JLabel();
        rbtPesFuncionario = new javax.swing.JRadioButton();
        rbtPesTecnico = new javax.swing.JRadioButton();
        rbtPesSetor = new javax.swing.JRadioButton();
        cboSetores = new javax.swing.JComboBox<>();
        lblLabel1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jLabel6.setText("jLabel6");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Ordem de Serviço");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 44)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 60));
        jLabel2.setText("Ordem de Serviço");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(300, 40, 390, 56);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48755_add_document_add_document.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(694, 40, 64, 64);

        txtPesSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesSet.setToolTipText("Pesquisar Setor");
        txtPesSet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesSetKeyReleased(evt);
            }
        });
        getContentPane().add(txtPesSet);
        txtPesSet.setBounds(340, 185, 180, 28);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 50));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/time.png"))); // NOI18N
        jLabel5.setText("Tempo:");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(666, 127, 82, 28);

        lblMulti.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblMulti.setForeground(new java.awt.Color(0, 0, 50));
        lblMulti.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMulti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        lblMulti.setText("Setor");
        getContentPane().add(lblMulti);
        lblMulti.setBounds(258, 185, 80, 28);

        txtTempo.setEditable(false);
        txtTempo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTempo.setForeground(new java.awt.Color(0, 0, 72));
        txtTempo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTempo.setText("00 - 00 : 00 : 00");
        getContentPane().add(txtTempo);
        txtTempo.setBounds(750, 127, 155, 28);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 50));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/date_time.png"))); // NOI18N
        jLabel7.setText("Início:");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(220, 127, 72, 28);

        txtInicio.setEditable(false);
        txtInicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtInicio);
        txtInicio.setBounds(294, 127, 140, 28);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 50));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/date_time.png"))); // NOI18N
        jLabel8.setText("Témino:");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(438, 127, 85, 28);

        txtTermino.setEditable(false);
        txtTermino.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTermino);
        txtTermino.setBounds(524, 127, 140, 28);

        tblSetor = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblSetor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Andar", "Setor"
            }
        ));
        tblSetor.setOpaque(false);
        tblSetor.getTableHeader().setReorderingAllowed(false);
        tblSetor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSetorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSetor);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(92, 215, 676, 100);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 50));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("N°O.S:");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(88, 127, 48, 28);

        txtNumOs.setEditable(false);
        txtNumOs.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtNumOs);
        txtNumOs.setBounds(138, 127, 78, 28);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/os2.png"))); // NOI18N
        jLabel4.setAlignmentX(0.5F);
        getContentPane().add(jLabel4);
        jLabel4.setBounds(67, 120, 855, 50);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 50));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Código:");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel11);
        jLabel11.setBounds(52, 376, 80, 20);

        txtIdSet.setEditable(false);
        txtIdSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdSet);
        txtIdSet.setBounds(52, 396, 80, 28);

        txtSetCham.setEditable(false);
        txtSetCham.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtSetCham);
        txtSetCham.setBounds(274, 396, 231, 28);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 50));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Andar:");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(138, 376, 130, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 50));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("*Solicitado por:");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(138, 328, 200, 20);

        txtNomFun.setEditable(false);
        txtNomFun.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtNomFun);
        txtNomFun.setBounds(138, 348, 200, 28);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 50));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("R.E:");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(52, 328, 80, 20);

        txtIdFun.setEditable(false);
        txtIdFun.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdFun);
        txtIdFun.setBounds(52, 348, 80, 28);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 50));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Observação:");
        getContentPane().add(jLabel16);
        jLabel16.setBounds(640, 460, 300, 20);

        lblPri.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPri.setForeground(new java.awt.Color(0, 0, 50));
        lblPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPri.setText("Prioridade");
        getContentPane().add(lblPri);
        lblPri.setBounds(722, 328, 104, 20);

        cboServico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Civil", "Elétrica", "Hidráulico", "Refrigeração" }));
        getContentPane().add(cboServico);
        cboServico.setBounds(515, 445, 104, 28);

        lblServico.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblServico.setForeground(new java.awt.Color(0, 0, 50));
        lblServico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblServico.setText("Serviço:");
        getContentPane().add(lblServico);
        lblServico.setBounds(515, 425, 104, 20);

        getContentPane().add(cboSeguimento);
        cboSeguimento.setBounds(515, 396, 104, 28);

        txtIdTec.setEditable(false);
        txtIdTec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdTec);
        txtIdTec.setBounds(52, 445, 80, 28);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 50));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("R.E:");
        getContentPane().add(jLabel18);
        jLabel18.setBounds(52, 425, 80, 20);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 50));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Especialodade:");
        getContentPane().add(jLabel19);
        jLabel19.setBounds(343, 425, 162, 20);

        txaObservacao.setColumns(20);
        txaObservacao.setLineWrap(true);
        txaObservacao.setRows(5);
        jScrollPane3.setViewportView(txaObservacao);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(640, 480, 300, 50);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 50));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("*Descrição:");
        getContentPane().add(jLabel20);
        jLabel20.setBounds(640, 376, 300, 20);

        txaDescricao.setColumns(20);
        txaDescricao.setLineWrap(true);
        txaDescricao.setRows(5);
        jScrollPane4.setViewportView(txaDescricao);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(640, 396, 300, 50);

        tblArquivos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblArquivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N° Arquivo", "Nome"
            }
        ));
        tblArquivos.getTableHeader().setResizingAllowed(false);
        tblArquivos.getTableHeader().setReorderingAllowed(false);
        tblArquivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblArquivosMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblArquivos);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(52, 516, 425, 106);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 50));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        jLabel21.setText("Arquivo");
        getContentPane().add(jLabel21);
        jLabel21.setBounds(52, 480, 84, 28);

        lblCarObs.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblCarObs.setText("contagem");
        getContentPane().add(lblCarObs);
        lblCarObs.setBounds(640, 526, 300, 18);

        lblCarDes.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblCarDes.setText("contagem");
        getContentPane().add(lblCarDes);
        lblCarDes.setBounds(640, 442, 300, 18);

        txtPesArq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesArq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesArqKeyReleased(evt);
            }
        });
        getContentPane().add(txtPesArq);
        txtPesArq.setBounds(138, 480, 160, 28);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 60));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Quantidade:");
        getContentPane().add(jLabel23);
        jLabel23.setBounds(308, 480, 86, 26);

        txtQuaArq.setEditable(false);
        txtQuaArq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtQuaArq.setText("0");
        getContentPane().add(txtQuaArq);
        txtQuaArq.setBounds(396, 480, 80, 28);

        btnIniOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48761_file_add_file_upload_add_upload.png"))); // NOI18N
        btnIniOs.setToolTipText("Iniciar O.S");
        btnIniOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniOsActionPerformed(evt);
            }
        });
        getContentPane().add(btnIniOs);
        btnIniOs.setBounds(542, 556, 64, 64);

        btnEncOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48753_diskette_diskette.png"))); // NOI18N
        btnEncOs.setToolTipText("Encerrar O.S");
        btnEncOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncOsActionPerformed(evt);
            }
        });
        getContentPane().add(btnEncOs);
        btnEncOs.setBounds(616, 556, 64, 64);

        btnAdcArqOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48755_add_document_add_document.png"))); // NOI18N
        btnAdcArqOs.setToolTipText("Adicionar Arquivo");
        btnAdcArqOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdcArqOsActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdcArqOs);
        btnAdcArqOs.setBounds(761, 556, 64, 64);

        btnImpOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpOs.setToolTipText("Imprimir O.S");
        btnImpOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpOsActionPerformed(evt);
            }
        });
        getContentPane().add(btnImpOs);
        btnImpOs.setBounds(832, 556, 64, 64);

        txtNomTec.setEditable(false);
        txtNomTec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtNomTec);
        txtNomTec.setBounds(138, 445, 200, 28);

        btnNovOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48760_file_file.png"))); // NOI18N
        btnNovOs.setToolTipText("Nova O.S");
        btnNovOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovOsActionPerformed(evt);
            }
        });
        getContentPane().add(btnNovOs);
        btnNovOs.setBounds(688, 556, 64, 64);

        txtAndSet.setEditable(false);
        txtAndSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtAndSet);
        txtAndSet.setBounds(138, 396, 130, 28);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 50));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Setor:");
        getContentPane().add(jLabel22);
        jLabel22.setBounds(272, 376, 231, 20);

        txtEspTec.setEditable(false);
        txtEspTec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtEspTec);
        txtEspTec.setBounds(343, 445, 162, 28);

        lblNomeCol.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNomeCol.setForeground(new java.awt.Color(0, 0, 50));
        lblNomeCol.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNomeCol.setText("Atendido pelo Colaborador");
        getContentPane().add(lblNomeCol);
        lblNomeCol.setBounds(138, 425, 200, 20);

        txtAreFun.setEditable(false);
        txtAreFun.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtAreFun);
        txtAreFun.setBounds(343, 348, 180, 28);

        lblSeguimento.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblSeguimento.setForeground(new java.awt.Color(0, 0, 50));
        lblSeguimento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSeguimento.setText("Seguimento:");
        getContentPane().add(lblSeguimento);
        lblSeguimento.setBounds(515, 376, 104, 20);

        txtTipFun.setEditable(false);
        txtTipFun.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTipFun);
        txtTipFun.setBounds(530, 348, 185, 28);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 50));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("*Área:");
        getContentPane().add(jLabel26);
        jLabel26.setBounds(343, 328, 200, 20);

        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "A e B ", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança" }));
        cboFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFilterActionPerformed(evt);
            }
        });
        getContentPane().add(cboFilter);
        cboFilter.setBounds(530, 185, 112, 28);

        cboPriOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Baixa", "Média", "Alta" }));
        cboPriOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPriOsActionPerformed(evt);
            }
        });
        getContentPane().add(cboPriOs);
        cboPriOs.setBounds(722, 348, 104, 28);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 50));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("*Cargo:");
        getContentPane().add(jLabel27);
        jLabel27.setBounds(530, 328, 185, 20);

        lblPrioridade.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrioridade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/verde.png"))); // NOI18N
        getContentPane().add(lblPrioridade);
        lblPrioridade.setBounds(812, 352, 28, 20);

        rbtPesFuncionario.setBackground(new java.awt.Color(255, 255, 255));
        btgPesquisa.add(rbtPesFuncionario);
        rbtPesFuncionario.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rbtPesFuncionario.setForeground(new java.awt.Color(0, 0, 50));
        rbtPesFuncionario.setText("Solicitado por");
        rbtPesFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtPesFuncionarioActionPerformed(evt);
            }
        });
        getContentPane().add(rbtPesFuncionario);
        rbtPesFuncionario.setBounds(786, 215, 130, 28);

        rbtPesTecnico.setBackground(new java.awt.Color(255, 255, 255));
        btgPesquisa.add(rbtPesTecnico);
        rbtPesTecnico.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rbtPesTecnico.setForeground(new java.awt.Color(0, 0, 50));
        rbtPesTecnico.setText("Atendido por");
        rbtPesTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtPesTecnicoActionPerformed(evt);
            }
        });
        getContentPane().add(rbtPesTecnico);
        rbtPesTecnico.setBounds(786, 286, 130, 28);

        rbtPesSetor.setBackground(new java.awt.Color(255, 255, 255));
        btgPesquisa.add(rbtPesSetor);
        rbtPesSetor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rbtPesSetor.setForeground(new java.awt.Color(0, 0, 50));
        rbtPesSetor.setText("Setor");
        rbtPesSetor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtPesSetorActionPerformed(evt);
            }
        });
        getContentPane().add(rbtPesSetor);
        rbtPesSetor.setBounds(786, 251, 130, 28);

        cboSetores.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboSetores.setForeground(new java.awt.Color(0, 0, 51));
        cboSetores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A e B (Alimentos e Bebidas)", "Administrativo", "Eventos", "Governança", "Manutenção", "Mordomia", "Recepção", "Segurança" }));
        cboSetores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSetoresActionPerformed(evt);
            }
        });
        getContentPane().add(cboSetores);
        cboSetores.setBounds(52, 56, 220, 28);

        lblLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/lblCadOs.png"))); // NOI18N
        lblLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblLabel1);
        lblLabel1.setBounds(38, 108, 920, 530);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Os0.jpg"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, -8, 1007, 672);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `btnAdcArqOsActionPerformed`. Executado quando o botão
     * `btnAdcArqOs` é clicado. Este método estabelece conexão com o banco de
     * dados, atualiza as informações da tabela de arquivos e inicializa ou
     * torna visível a janela de gerenciamento de arquivos. Caso a janela
     * `Arquivos` já esteja aberta, apenas garante que ela esteja visível.
     *
     * @param evt Evento de ação associado ao clique do botão.
     */
    private void btnAdcArqOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdcArqOsActionPerformed

        conexao = ModuloConexao.conector();
        if (arquivos == null) {
            txtPesArq.setText("");
            arquivosBuscaIni();
            idOs = txtNumOs.getText();
            arquivos = new Arquivos();
            arquivos.setVisible(true);
            iniciarAtualizacao();

        } else {
            arquivos.setVisible(true);
        }
    }//GEN-LAST:event_btnAdcArqOsActionPerformed

    /**
     * Método `btnIniOsActionPerformed`. Executado ao clicar no botão
     * `btnIniOs`, este método chama o método `iniciarOs()` para realizar a
     * inicialização de uma nova Ordem de Serviço (OS).
     *
     * @param evt Evento de ação associado ao clique no botão.
     */
    private void btnIniOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniOsActionPerformed
        // chama o metodo iniciar ordem de serviço
        iniciarOs();
    }//GEN-LAST:event_btnIniOsActionPerformed

    /**
     * Método `formInternalFrameOpened`. Executado automaticamente quando o
     * frame interno é aberto. Este método aplica regras de inicialização para
     * configurar os componentes da interface gráfica e preparar o ambiente para
     * o uso, incluindo a tabela de arquivos e botões da Ordem de Serviço (OS).
     *
     * @param evt Evento associado à abertura do frame interno.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // regras se inicialização:
        VerificaTabela();
        btnEncOs.setEnabled(false);
        btnNovOs.setEnabled(false);
        btnAdcArqOs.setEnabled(false);
        btnImpOs.setEnabled(false);
        ((DefaultTableModel) tblArquivos.getModel()).setRowCount(0);

    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método `txtPesSetKeyReleased`. Executado ao liberar uma tecla enquanto o
     * campo de texto `txtPesSet` está em foco. Este método verifica o valor da
     * variável `Pesq` e chama o método apropriado para realizar uma pesquisa
     * dinâmica (funcionários, setores ou técnicos).
     *
     * @param evt Evento associado ao pressionamento de teclas no campo de
     * texto.
     */
    private void txtPesSetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesSetKeyReleased
        // metodo de pesquisa dinamico de setores
        if (Pesq.equals("Funcionário")) {
            funcionarios();
        }
        if (Pesq.equals("Setor")) {
            pesquisarSetor();
        }
        if (Pesq.equals("Técnico")) {
            pesquisarTecnico();
        }

    }//GEN-LAST:event_txtPesSetKeyReleased

    /**
     * Método `tblSetorMouseClicked`. Executado ao clicar em uma linha da tabela
     * `tblSetor`. Este método identifica o valor da variável `Pesq` e chama o
     * método apropriado para realizar as ações relacionadas, como selecionar
     * funcionários, setores ou técnicos.
     *
     * @param evt Evento associado ao clique do mouse na tabela.
     */
    private void tblSetorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSetorMouseClicked
        // chama o metodo que realiza a pesquisa do setor e logo em seguida chama o método que atualiza a tabela para os funcionarios:
        if (Pesq.equals("Funcionário")) {
            setarFuncionario();
        }
        if (Pesq.equals("Setor")) {
            setarSetor();
        }
        if (Pesq.equals("Técnico")) {
            setarTecnico();
        }

    }//GEN-LAST:event_tblSetorMouseClicked

    /**
     * Método `cboFilterActionPerformed`. Executado ao selecionar um item no
     * ComboBox `cboFilter`. Este método direciona a lógica de filtragem para o
     * método apropriado com base no valor selecionado. Ele aplica o filtro de
     * pesquisa na tabela e ajusta a capacidade de edição do campo de texto
     * `txtPesSet`.
     *
     * @param evt Evento associado à seleção de um item no ComboBox.
     */
    private void cboFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFilterActionPerformed
        //direciona o método a ser utilizado ao aplicar o filtro para pesquisa:
        String string;
        string = cboFilter.getSelectedItem().toString();
        txtPesSet.setText("");

        if (string.equals("Todos")) {
            funcionariosIni();
            cboFilter.setSelectedItem("Todos");
            txtPesSet.setEditable(false);
        } else {
            funcionarios();
            txtPesSet.setEditable(true);
            txtPesSet.requestFocus();

        }
    }//GEN-LAST:event_cboFilterActionPerformed

    /**
     * Método `btnEncOsActionPerformed`. Executado ao clicar no botão
     * `btnEncOs`, este método chama o método `fecharOs()` para realizar o
     * encerramento de uma Ordem de Serviço (OS) ativa.
     *
     * @param evt Evento de ação associado ao clique no botão.
     */
    private void btnEncOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncOsActionPerformed
        // chama o meétodo que encerra a ordem de serviço:
        fecharOs();
    }//GEN-LAST:event_btnEncOsActionPerformed

    /**
     * Método `btnNovOsActionPerformed`. Executado ao clicar no botão
     * `btnNovOs`, este método prepara a interface para iniciar uma nova Ordem
     * de Serviço (OS). Ele estabelece uma conexão com o banco de dados, encerra
     * a janela de arquivos aberta (caso exista) e chama o método `limpar()`
     * para reinicializar os componentes da interface.
     *
     * @param evt Evento de ação associado ao clique no botão.
     */
    private void btnNovOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovOsActionPerformed
        // chama o metodo limpar para iniciar uma nova ordem de serviço:
        conexao = ModuloConexao.conector();
        if (arquivos != null) {
            arquivos.encerrar();
        }
        limpar();
    }//GEN-LAST:event_btnNovOsActionPerformed

    /**
     * Método `txtPesArqKeyReleased`. Executado ao liberar uma tecla enquanto o
     * campo de texto `txtPesArq` está em foco. Este método realiza uma busca
     * dinâmica de arquivos associados à Ordem de Serviço (OS), alternando entre
     * a atualização periódica automática e a busca manual, dependendo do
     * conteúdo digitado no campo.
     *
     * @param evt Evento associado ao pressionamento de teclas no campo de
     * texto.
     */
    private void txtPesArqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesArqKeyReleased
        // metodo de busca dinamica de arquivos da ordem de serviço:
        String com = txtPesArq.getText();
        if (situacaoOs.equals("Encerrada")) {
            if ("".equals(com)) {
                atualizar = true;
                arquivosBuscaIni();
            } else {
                segu = 0;
                atualizar = false;
                arquivosBusca();
            }
        } else {
            if ("".equals(com)) {
                atualizar = true;
                arquivosBuscaIni();
                iniciarAtualizacao();
            } else {
                segu = 0;
                atualizar = false;
                arquivosBusca();
                pararAtualizacao();
            }
        }

    }//GEN-LAST:event_txtPesArqKeyReleased

    /**
     * Método `tblArquivosMouseClicked`. Executado ao clicar em uma linha da
     * tabela `tblArquivos`. Este método utiliza o `StringBuilder` para
     * manipular o texto do campo `txtPesArq` antes de realizar ações
     * relacionadas ao arquivo selecionado. Ele também verifica se a janela de
     * arquivos já foi inicializada e realiza as tarefas correspondentes, como
     * seleção do arquivo e reinicialização da busca automática.
     *
     * @param evt Evento associado ao clique do mouse na tabela.
     */
    private void tblArquivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblArquivosMouseClicked

        buff.append(txtPesArq.getText());
        if (situacaoOs.equals("Encerrada")) {
            if (arquivos == null) {
                setArquivoOs();
                arquivos = new Arquivos();
                arquivos.setVisible(true);
                arquivos.selecionarArquivo();
                txtPesArq.setText("");
                arquivosBuscaIni();

            } else {
                setArquivoOs();
                arquivos.selecionarArquivo();
                arquivos.setVisible(true);
                txtPesArq.setText("");
                arquivosBuscaIni();
            }
        } else {
            if (arquivos == null) {
                setArquivoOs();
                arquivos = new Arquivos();
                arquivos.setVisible(true);
                arquivos.selecionarArquivo();
                txtPesArq.setText("");
                arquivosBuscaIni();
                iniciarAtualizacao();

            } else {
                setArquivoOs();
                arquivos.selecionarArquivo();
                arquivos.setVisible(true);
                txtPesArq.setText("");
                arquivosBuscaIni();
                iniciarAtualizacao();
            }
        }

    }//GEN-LAST:event_tblArquivosMouseClicked

    /**
     * Método `formInternalFrameClosing`. Executado automaticamente quando o
     * frame interno está sendo fechado. Este método verifica se a janela de
     * arquivos (`arquivos`) está aberta e, caso esteja, invoca o método
     * `encerrar()` para garantir que seus recursos sejam liberados
     * corretamente.
     *
     * @param evt Evento associado ao fechamento do frame interno.
     */
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        pararAtualizacao();
        if (arquivos != null) {
            arquivos.encerrar();
        }
    }//GEN-LAST:event_formInternalFrameClosing

    /**
     * Método `cboPriOsActionPerformed`. Executado ao selecionar um item no
     * ComboBox `cboPriOs`. Este método ajusta o ícone exibido no
     * `lblPrioridade` com base no nível de prioridade selecionado (Baixa, Média
     * ou Alta). Cada nível de prioridade é associado a uma cor específica
     * (verde, amarelo ou vermelho).
     *
     * @param evt Evento associado à seleção de um item no ComboBox.
     */
    private void cboPriOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPriOsActionPerformed
        // define a cor da prioridade:
        String comparacao;
        comparacao = cboPriOs.getSelectedItem().toString();
        if (comparacao.equals("Baixa")) {
            lblPrioridade.setIcon(new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/verde.png")));
        }
        if (comparacao.equals("Média")) {
            lblPrioridade.setIcon(new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/amarelo.png")));
        }
        if (comparacao.equals("Alta")) {
            lblPrioridade.setIcon(new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/vermelho.png")));
        }
    }//GEN-LAST:event_cboPriOsActionPerformed

    /**
     * Método `rbtPesTecnicoActionPerformed`. Executado ao selecionar o botão de
     * rádio `rbtPesTecnico`. Este método ajusta a interface para realizar
     * pesquisas iniciais de técnicos, desativando e redefinindo componentes
     * relevantes e mudando as cores dos botões para destacar a seleção atual.
     *
     * @param evt Evento associado à seleção do botão de rádio.
     */
    private void rbtPesTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtPesTecnicoActionPerformed

        cboFilter.setEnabled(false);
        cboFilter.setSelectedItem("Todos");
        txtPesSet.setEditable(true);
        txtPesSet.setText("");
        rbtPesTecnico.setForeground(new Color(39, 44, 182));
        rbtPesFuncionario.setForeground(new Color(0, 0, 50));
        rbtPesSetor.setForeground(new Color(0, 0, 50));
        pesquisarTecnicoIni();
    }//GEN-LAST:event_rbtPesTecnicoActionPerformed

    /**
     * Método `rbtPesFuncionarioActionPerformed`. Executado ao selecionar o
     * botão de rádio `rbtPesFuncionario`. Este método ajusta a interface para
     * realizar pesquisas iniciais de funcionários, ativando o ComboBox de
     * filtro e desativando o campo de texto de pesquisa. Também altera as cores
     * dos botões de rádio para destacar a seleção atual.
     *
     * @param evt Evento associado à seleção do botão de rádio.
     */
    private void rbtPesFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtPesFuncionarioActionPerformed

        cboFilter.setEnabled(true);
        txtPesSet.setEditable(false);
        rbtPesTecnico.setForeground(new Color(0, 0, 50));
        rbtPesFuncionario.setForeground(new Color(39, 44, 182));
        rbtPesSetor.setForeground(new Color(0, 0, 50));
        funcionariosIni();
    }//GEN-LAST:event_rbtPesFuncionarioActionPerformed

    /**
     * Método `rbtPesSetorActionPerformed`. Executado ao selecionar o botão de
     * rádio `rbtPesSetor`. Este método ajusta a interface do usuário para a
     * pesquisa inicial de setores, desativando o ComboBox de filtros,
     * habilitando o campo de texto para entrada manual, e destacando
     * visualmente o botão de rádio selecionado.
     *
     * @param evt Evento associado à seleção do botão de rádio.
     */
    private void rbtPesSetorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtPesSetorActionPerformed

        cboFilter.setEnabled(false);
        cboFilter.setSelectedItem("Todos");
        txtPesSet.setEditable(true);
        txtPesSet.setText("");
        rbtPesTecnico.setForeground(new Color(0, 0, 50));
        rbtPesFuncionario.setForeground(new Color(0, 0, 50));
        rbtPesSetor.setForeground(new Color(39, 44, 182));
        pesquisarSetorIni();
    }//GEN-LAST:event_rbtPesSetorActionPerformed

    /**
     * Método `cboSetoresActionPerformed`. Executado ao selecionar um item no
     * ComboBox `cboSetores`. Este método ajusta a interface para refletir o
     * setor selecionado, redefine os botões de pesquisa, habilita ou desabilita
     * componentes conforme necessário e prepara os dados relacionados ao setor
     * para exibição.
     *
     * @param evt Evento associado à seleção de um item no ComboBox.
     */
    private void cboSetoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSetoresActionPerformed

        selct = cboSetores.getSelectedItem().toString();
        rbtPesFuncionario.setSelected(true);
        cboFilter.setEnabled(true);
        txtPesSet.setEditable(false);
        rbtPesTecnico.setForeground(new Color(0, 0, 50));
        rbtPesFuncionario.setForeground(new Color(39, 44, 182));
        rbtPesSetor.setForeground(new Color(0, 0, 50));
        funcionariosIni();
        limpar();
        selecSetor();
    }//GEN-LAST:event_cboSetoresActionPerformed

    /**
     * Método `btnImpOsActionPerformed`. Executado ao clicar no botão
     * `btnImpOs`. Este método chama o método `imprimir()` para gerar e
     * visualizar o relatório associado à Ordem de Serviço (OS) atual.
     *
     * @param evt Evento de ação associado ao clique do botão.
     */
    private void btnImpOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpOsActionPerformed

        imprimir();
    }//GEN-LAST:event_btnImpOsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgPesquisa;
    private javax.swing.JButton btnAdcArqOs;
    private javax.swing.JButton btnEncOs;
    private javax.swing.JButton btnImpOs;
    private javax.swing.JButton btnIniOs;
    private javax.swing.JButton btnNovOs;
    private javax.swing.JComboBox<String> cboFilter;
    private javax.swing.JComboBox<String> cboPriOs;
    private javax.swing.JComboBox<String> cboSeguimento;
    private javax.swing.JComboBox<String> cboServico;
    private javax.swing.JComboBox<String> cboSetores;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblCarDes;
    private javax.swing.JLabel lblCarObs;
    private javax.swing.JLabel lblLabel1;
    private javax.swing.JLabel lblMulti;
    private javax.swing.JLabel lblNomeCol;
    private javax.swing.JLabel lblPri;
    private javax.swing.JLabel lblPrioridade;
    private javax.swing.JLabel lblSeguimento;
    private javax.swing.JLabel lblServico;
    private javax.swing.JRadioButton rbtPesFuncionario;
    private javax.swing.JRadioButton rbtPesSetor;
    private javax.swing.JRadioButton rbtPesTecnico;
    private javax.swing.JTable tblArquivos;
    private javax.swing.JTable tblSetor;
    private javax.swing.JTextArea txaDescricao;
    private javax.swing.JTextArea txaObservacao;
    private javax.swing.JTextField txtAndSet;
    private javax.swing.JTextField txtAreFun;
    private javax.swing.JTextField txtEspTec;
    private javax.swing.JTextField txtIdFun;
    private javax.swing.JTextField txtIdSet;
    private javax.swing.JTextField txtIdTec;
    private javax.swing.JTextField txtInicio;
    private javax.swing.JTextField txtNomFun;
    private javax.swing.JTextField txtNomTec;
    private javax.swing.JTextField txtNumOs;
    private javax.swing.JTextField txtPesArq;
    private javax.swing.JTextField txtPesSet;
    private javax.swing.JTextField txtQuaArq;
    private javax.swing.JTextField txtSetCham;
    private javax.swing.JTextField txtTempo;
    private javax.swing.JTextField txtTermino;
    private javax.swing.JTextField txtTipFun;
    // End of variables declaration//GEN-END:variables
}
