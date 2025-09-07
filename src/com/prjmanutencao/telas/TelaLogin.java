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
import static com.prjmanutencao.telas.TelaAcesso.nome;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Realiza a autenticação de usuário e senha.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaLogin extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String ipDaMaquina = null;
    String nomeDaMaquina = null;

    public static String id = null;
    public static String tip = null;

    /**
     * Método responsável por realizar o login do usuário no sistema. Valida o
     * login e senha informados contra os registros no banco de dados.
     */
    public void logar() {

        ip_maqui();
        String senha;
        String sql = "select * from usuarios where login_usuario=? and senha_usuario=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUsuario.getText());
            senha = new String(txtSenha.getPassword());
            pst.setString(2, senha);
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getString(1);
                nome = rs.getString(2);
                String perfil = rs.getString(3);
                tip = rs.getString(3);
                acesso();
                if (perfil.equals("Superadmin")) {
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    TelaPrincipal.menCad.setEnabled(true);
                    TelaPrincipal.menPreSemEnc.setEnabled(true);
                    TelaPrincipal.menRel.setEnabled(true);
                    TelaPrincipal.menPreAbr.setEnabled(true);
                    TelaPrincipal.lblUsuario.setText("Olá".concat(" ").concat(rs.getString(2).split(" ")[0]));
                    TelaPrincipal.lblUsuario.setForeground(new Color(74, 1, 56));
                    TelaPrincipal.menOpcAce.setEnabled(true);
                    statusUsario();
                    this.dispose();
                    conexao.close();

                }
                if (perfil.equals("Admin")) {
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    TelaPrincipal.menCad.setEnabled(true);
                    TelaPrincipal.menPreSemEnc.setEnabled(true);
                    TelaPrincipal.menPreAbr.setEnabled(true);
                    TelaPrincipal.lblUsuario.setText("Olá".concat(" ").concat(rs.getString(2).split(" ")[0]));
                    TelaPrincipal.lblUsuario.setForeground(new Color(0, 74, 0));
                    statusUsario();
                    this.dispose();
                    conexao.close();

                }
                if (perfil.equals("User")) {
                    TelaPrincipal principal = new TelaPrincipal();
                    TelaPrincipal.menOsAbeFec.setEnabled(false);
                    principal.setVisible(true);
                    TelaPrincipal.lblUsuario.setText("Olá".concat(" ").concat(rs.getString(2).split(" ")[0]));
                    TelaPrincipal.lblUsuario.setForeground(new Color(222, 0, 0));
                    statusUsario();
                    this.dispose();
                    conexao.close();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usuário e/ou senha inválido!");

            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por atualizar o status do usuário no banco de dados. O
     * status é definido como "on-line" para o usuário logado atualmente.
     */
    private void statusUsario() {

        String str = "on-line";
        String sql = "update usuarios set status_usuario = ? where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, str);
            pst.setString(2, id);
            int alterar1 = pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Construtor da classe TelaLogin. É responsável por inicializar os
     * componentes da interface e verificar a conexão com o banco de dados.
     */
    public TelaLogin() {
        initComponents();
        setIcon();
        conexao = ModuloConexao.conector();
        if (conexao != null) {
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/dbok.png")));
        } else {
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/dberro.png")));
        }
    }

    /**
     * Método responsável por capturar o endereço IP e o nome da máquina local.
     * Utiliza as classes da API Java para obter informações de rede da máquina.
     */
    private void ip_maqui() {

        try {
            //pegamos o ip da maquina.
            ipDaMaquina = InetAddress.getLocalHost().getHostAddress();
            //nome da maquina.
            nomeDaMaquina = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por capturar o endereço IP e o nome da máquina local.
     * Utiliza as classes da API Java para obter informações de rede da máquina.
     */
    private void acesso() {

        String data;

        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        data = sdf3.format(timestamp);

        String sql = "insert into acesso (ip_maqui, nome_maqui, iduser, data_entr) values (?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, ipDaMaquina);
            pst.setString(2, nomeDaMaquina);
            pst.setString(3, id);
            pst.setString(4, data);
            int Adicionar = pst.executeUpdate();
            if (Adicionar > 0) {
            }

        } catch (SQLException ex) {
            Logger.getLogger(TelaLogin.class.getName()).log(Level.SEVERE, null, ex);
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

        txtSenha = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jclSen = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema - Login");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        txtSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtSenha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSenhaKeyTyped(evt);
            }
        });
        getContentPane().add(txtSenha);
        txtSenha.setBounds(270, 240, 250, 30);

        btnLogin.setBackground(new java.awt.Color(191, 191, 191));
        btnLogin.setFont(new java.awt.Font("Arial Black", 1, 16)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(51, 51, 255));
        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/55827_lock_padlock_private_icon.png"))); // NOI18N
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogin);
        btnLogin.setBounds(390, 300, 130, 40);

        lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/dberro.png"))); // NOI18N
        getContentPane().add(lblStatus);
        lblStatus.setBounds(340, 300, 40, 40);

        txtUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuario.setName(""); // NOI18N
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyTyped(evt);
            }
        });
        getContentPane().add(txtUsuario);
        txtUsuario.setBounds(270, 170, 250, 30);
        txtUsuario.getAccessibleContext().setAccessibleName("");

        jclSen.setOpaque(false);
        jclSen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jclSenActionPerformed(evt);
            }
        });
        getContentPane().add(jclSen);
        jclSen.setBounds(270, 280, 20, 20);

        jLabel1.setText("Mostrar Senha");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(292, 284, 90, 14);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/2.jpg"))); // NOI18N
        jLabel2.setDoubleBuffered(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 600, 400);

        setSize(new java.awt.Dimension(600, 400));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento acionado ao clicar no botão "Login". Verifica se há conexão com o
     * banco de dados e executa o processo de login.
     *
     * @param evt Objeto que contém informações sobre o evento de clique no
     * botão.
     */
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed

        if (conexao != null) {
            logar();
        } else {
            JOptionPane.showMessageDialog(null, "Você esta sem Conexão com o Banco de Dados!" + "\n" + "Verifique a sua Internet e/ou Servidor." + "\n" + "Entre em Contato com o Suporte.", null, 2);
        }

    }//GEN-LAST:event_btnLoginActionPerformed

    /**
     * Evento acionado ao clicar no componente "jclSen". Este método alterna a
     * exibição da senha no campo de texto (mostrar ou ocultar).
     *
     * @param evt Objeto que contém informações sobre o evento acionado.
     */
    private void jclSenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jclSenActionPerformed

        if (txtSenha.getEchoChar() == '●') {
            txtSenha.setEchoChar((char) 0);
        } else {
            txtSenha.setEchoChar('●');
        }

    }//GEN-LAST:event_jclSenActionPerformed

    /**
     * Evento acionado ao digitar uma tecla no campo "txtUsuario". Permite
     * apenas determinados caracteres serem inseridos no campo.
     *
     * @param evt Objeto que contém informações sobre o evento de tecla
     * pressionada.
     */
    private void txtUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyTyped

        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz_@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtUsuarioKeyTyped

    /**
     * Evento acionado ao digitar uma tecla no campo "txtSenha". Permite apenas
     * números serem inseridos no campo de texto da senha.
     *
     * @param evt Objeto que contém informações sobre o evento de tecla
     * pressionada.
     */
    private void txtSenhaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyTyped

        String caracter = "0123456789";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtSenhaKeyTyped

    /**
     * Evento acionado quando a janela é aberta. Configura o campo de texto da
     * senha para exibir o caractere de máscara ('●'), ocultando os caracteres
     * digitados pelo usuário.
     *
     * @param evt Objeto que contém informações sobre o evento de abertura da
     * janela.
     */
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        txtSenha.setEchoChar('●');
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox jclSen;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    /**
     * Método responsável por definir o ícone principal da aplicação. Utiliza a
     * classe Toolkit para carregar a imagem do recurso especificado.
     */
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png")));
    }
}
