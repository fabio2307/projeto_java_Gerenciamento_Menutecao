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
import java.sql.*;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Classe TelaAcesso
 *
 * Representa uma janela interna (JInternalFrame) responsável pelo controle de
 * acesso de usuários.
 *
 * Funcionalidade: - Realiza conexão com o banco de dados para autenticação. -
 * Utiliza PreparedStatement para consultas seguras. - Armazena resultados em
 * ResultSet para manipulação posterior. - Pode exibir dados em uma tabela
 * (JTable) com modelo padrão.
 *
 * Atributos: - conexao: conexão ativa com o banco de dados. - pst: comando SQL
 * preparado para execução. - rs: resultado da consulta SQL. - data, teste:
 * variáveis auxiliares. - nome: nome do usuário logado, acessível de forma
 * estática. - DefaultTableModel: referência à tabela de dados exibida na
 * interface.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaAcesso extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String data, teste;
    public static String nome = null;
    JTable DefaultTableModel = null;

    /**
     * Construtor da classe TelaAcesso.
     *
     * Inicializa os componentes gráficos da interface. Estabelece a conexão com
     * o banco de dados. Realiza a consulta inicial de acessos. Alinha os
     * elementos visuais da interface. Define o modo de seleção da tabela como
     * seleção única. Configura a barra de rolagem vertical do painel para
     * sempre visível.
     */
    public TelaAcesso() {
        initComponents();
        conexao = ModuloConexao.conector();
        consultaAcesso();
        alinhar();
        tblAcesso.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
    }

    /**
     * Classe JlabelGradient
     *
     * Extende JLabel para criar um componente com fundo em gradiente vertical.
     * O gradiente é desenhado do topo para a base, indo de uma cor cinza
     * semi-transparente até branco, proporcionando um efeito visual suave.
     */
    class JlabelGradient extends JLabel {

        /**
         * Desenha o fundo do componente com um gradiente vertical.
         *
         * Este método sobrescreve paintComponent para aplicar um estilo
         * personalizado. Utiliza Graphics2D para criar um preenchimento com
         * gradiente entre duas cores.
         *
         * Parametros: estilo - objeto Graphics usado para renderizar o
         * componente.
         */
        @Override
        protected void paintComponent(Graphics estilo) {

            Graphics2D g2d = (Graphics2D) estilo;
            int width = getWidth();
            int height = getHeight();
            Color cor1 = new Color(158, 158, 158, 90);
            Color cor2 = new Color(255, 255, 255);
            GradientPaint gp = new GradientPaint(0, 0, cor1, 0, height, cor2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);

        }
    }

    /**
     * Ajusta o alinhamento e a largura das colunas da tabela tblAcesso.
     *
     * Define larguras específicas para cada coluna, desativa o
     * redimensionamento automático e centraliza o conteúdo das células e do
     * cabeçalho.
     *
     * Aplica um renderizador centralizado para todas as colunas da tabela.
     */
    private void alinhar() {

        int width = 80, width1 = 100, width2 = 120, width3 = 145, width4 = 145, width5 = 80, width6 = 159, width7 = 60;

        JTableHeader header = tblAcesso.getTableHeader();
        DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        tblAcesso.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblAcesso.getColumnModel().getColumn(0).setPreferredWidth(width);
        tblAcesso.getColumnModel().getColumn(1).setPreferredWidth(width1);
        tblAcesso.getColumnModel().getColumn(2).setPreferredWidth(width2);
        tblAcesso.getColumnModel().getColumn(3).setPreferredWidth(width3);
        tblAcesso.getColumnModel().getColumn(4).setPreferredWidth(width4);
        tblAcesso.getColumnModel().getColumn(5).setPreferredWidth(width5);
        tblAcesso.getColumnModel().getColumn(6).setPreferredWidth(width6);
        tblAcesso.getColumnModel().getColumn(7).setPreferredWidth(width7);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
        for (int i = 0; i < tblAcesso.getColumnCount(); i++) {
            tblAcesso.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Executa uma consulta SQL para obter os registros de acesso dos usuários.
     *
     * Realiza um inner join entre as tabelas de acesso e usuários. Os dados
     * retornados são organizados e exibidos na tabela tblAcesso.
     *
     * Em caso de erro na execução da consulta, uma mensagem de erro é exibida.
     */
    private void consultaAcesso() {

        String sql = "select a.id_ace as 'N° Acesso', a.ip_maqui as 'N° IP', a.nome_maqui as 'Máquina', a.data_entr as 'Entrada', a.data_said as 'Saída', u.iduser as 'R.E', u.nome_usuario as 'Nome', u.status_usuario as 'Status' from acesso a inner join  usuarios u on a.iduser = u.iduser order by a.id_ace asc";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tblAcesso.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Realiza uma pesquisa de acessos com base na data selecionada.
     *
     * Obtém a data do componente cadPesAce, formata no padrão yyyy-MM-dd e
     * executa uma consulta SQL filtrando os registros pela data de entrada.
     *
     * Os resultados são exibidos na tabela tblAcesso e alinhados com o método
     * alinhar. Em caso de erro na consulta, uma mensagem de erro é exibida.
     */
    private void pesquisarAcesso() {
        data = null;

        java.util.Date pega = cadPesAce.getDate();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        this.data = formato.format(pega);

        String sql = "select a.id_ace as 'N° Acesso', a.ip_maqui as 'N° IP', a.nome_maqui as 'Máquina', a.data_entr as 'Entrada', a.data_said as 'Saída', u.iduser as 'R.E', u.nome_usuario as 'Nome', u.status_usuario as 'Status' from acesso a inner join  usuarios u on a.iduser = u.iduser where a.data_entr like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, data + "%");
            rs = pst.executeQuery();
            tblAcesso.setModel(DbUtils.resultSetToTableModel(rs));
            alinhar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Define os dados do usuário selecionado na tabela tblAcesso.
     *
     * Obtém os valores das colunas correspondentes ao registro e nome do
     * usuário e os insere nos campos txtRegUsu e txtNomUsu.
     *
     * Se o campo txtRegUsu não estiver vazio, habilita o botão de impressão. Em
     * seguida, atualiza os dados da tabela e realinha as colunas.
     */
    private void setarAcesso() {

        int setar = tblAcesso.getSelectedRow();
        txtRegUsu.setText(tblAcesso.getModel().getValueAt(setar, 5).toString());
        txtNomUsu.setText(tblAcesso.getModel().getValueAt(setar, 6).toString());
        if (txtRegUsu != null) {
            btnImpAcesso.setEnabled(true);
        }
        consultaAcesso();
        alinhar();
    }

    private void imprimir() {

        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a Impressão?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                InputStream caminhoRelativo = getClass().getResourceAsStream("/imprimir/Acesso.jasper");
                HashMap filtro = new HashMap();
                JasperPrint print;
                JasperViewer viewer;
                Image icone;

                filtro.put("id", Integer.parseInt(txtRegUsu.getText()));
                print = JasperFillManager.fillReport(caminhoRelativo, filtro, conexao);
                viewer = new JasperViewer(print, false);
                // Define o título da janela
                viewer.setTitle("Meu Relatório");
                icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));
                viewer.setIconImage(icone);
                viewer.setVisible(true);
                txtNomUsu.setText(null);
                txtRegUsu.setText(null);
                btnImpAcesso.setEnabled(false);

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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblAcesso = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cadPesAce = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNomUsu = new javax.swing.JTextField();
        txtRegUsu = new javax.swing.JTextField();
        btnImpAcesso = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        JLabel = new JlabelGradient();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Lista de Acessos");
        setPreferredSize(new java.awt.Dimension(1007, 672));
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

        tblAcesso = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblAcesso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblAcesso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N° Acesso", "IP da Máquina", "Nome da Máquina", "Entrada", "Saída", "R.E", "Usuário"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblAcesso.getTableHeader().setReorderingAllowed(false);
        tblAcesso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAcessoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAcesso);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(42, 126, 910, 340);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Lista de Acessos");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(320, 50, 310, 50);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48798_file operations_file operations.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(630, 40, 80, 70);

        cadPesAce.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    pesquisarAcesso();

                }
            }
        });
        getContentPane().add(cadPesAce);
        cadPesAce.setBounds(120, 66, 140, 28);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(51, 51, 51), new java.awt.Color(102, 102, 102)));
        jPanel1.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("R.E");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 0, 70, 20);

        jLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel.setForeground(new java.awt.Color(0, 0, 51));
        jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel.setText("Usuário");
        jPanel1.add(jLabel);
        jLabel.setBounds(54, 42, 70, 20);

        jLabel3.setText("jLabel3");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(310, 520, 110, 14);

        txtNomUsu.setEditable(false);
        txtNomUsu.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        txtNomUsu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtNomUsu);
        txtNomUsu.setBounds(10, 60, 156, 24);

        txtRegUsu.setEditable(false);
        txtRegUsu.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        txtRegUsu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtRegUsu);
        txtRegUsu.setBounds(10, 20, 64, 24);

        btnImpAcesso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpAcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpAcessoActionPerformed(evt);
            }
        });
        jPanel1.add(btnImpAcesso);
        btnImpAcesso.setBounds(190, 14, 64, 64);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(670, 500, 280, 90);

        btnVoltar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnVoltar.setForeground(new java.awt.Color(0, 0, 51));
        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });
        getContentPane().add(btnVoltar);
        btnVoltar.setBounds(120, 100, 85, 23);
        getContentPane().add(JLabel);
        JLabel.setBounds(0, 0, 1000, 650);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Trata o evento de clique do mouse na tabela tblAcesso.
     *
     * Ao clicar em uma linha da tabela, os dados dessa linha são carregados nos
     * campos correspondentes por meio do método setarAcesso. Em seguida, o
     * método alinhar é chamado para ajustar a exibição da tabela.
     *
     * @param evt o evento de clique do mouse
     */
    private void tblAcessoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAcessoMouseClicked

        setarAcesso();
        alinhar();
    }//GEN-LAST:event_tblAcessoMouseClicked

    /**
     * Trata o evento de clique no botão Voltar.
     *
     * Recarrega os dados de acesso na tabela por meio do método consultaAcesso
     * e ajusta a exibição das colunas com o método alinhar.
     *
     * @param evt o evento de ação gerado pelo botão
     */
    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed

        consultaAcesso();
        alinhar();
    }//GEN-LAST:event_btnVoltarActionPerformed

    /**
     * Executado quando o frame interno é aberto.
     *
     * Define um ícone para o componente cadPesAce e desabilita o botão de
     * impressão.
     *
     * @param evt o evento de abertura do frame interno
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        ImageIcon icon = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/data.png"));
        cadPesAce.setIcon(icon);
        btnImpAcesso.setEnabled(false);
    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Trata o evento de clique no botão de impressão de acesso.
     *
     * Ao ser acionado, chama o método imprimir para gerar ou exibir o relatório
     * de acesso do usuário selecionado.
     *
     * @param evt o evento de ação gerado pelo botão
     */
    private void btnImpAcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpAcessoActionPerformed

        imprimir();
    }//GEN-LAST:event_btnImpAcessoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel;
    private javax.swing.JButton btnImpAcesso;
    private javax.swing.JButton btnVoltar;
    private com.toedter.calendar.JDateChooser cadPesAce;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tblAcesso;
    private javax.swing.JTextField txtNomUsu;
    private javax.swing.JTextField txtRegUsu;
    // End of variables declaration//GEN-END:variables
}
