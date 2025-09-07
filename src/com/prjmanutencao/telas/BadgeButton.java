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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Classe BadgeButton
 *
 * Estende JButton para incluir um indicador visual adicional, conhecido como
 * "badge". Esse badge pode ser usado para exibir notificações, contadores ou
 * alertas sobre o botão.
 *
 * Funcionalidade: - Permite desenhar um badge sobre o botão usando recursos
 * gráficos personalizados. - Pode ser usado em interfaces que exigem destaque
 * visual para ações pendentes ou novas mensagens.
 *
 * Requisitos: - Deve ser integrado a um layout Swing. - Pode exigir
 * sobreposição gráfica com Graphics2D para renderizar o badge.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class BadgeButton extends JButton {

    /**
     * Retorna a cor usada para desenhar o badge.
     *
     * Essa cor define o aspecto visual do indicador sobre o botão, podendo ser
     * personalizada conforme a necessidade da interface.
     *
     * Retorna: - A cor atual do badge.
     */
    public Color getBadgeColor() {
        return badgeColor;
    }

    /**
     * Define a cor usada para desenhar o badge.
     *
     * Essa cor será aplicada ao indicador visual sobre o botão, permitindo
     * personalização conforme o estilo da interface.
     *
     * Parâmetros: - badgeColor: nova cor a ser usada no badge.
     */
    public void setBadgeColor(Color badgeColor) {
        this.badgeColor = badgeColor;
    }

    /**
     * Imagem exibida no botão.
     *
     * Pode representar um ícone, ilustração ou qualquer elemento visual que
     * complemente a funcionalidade do botão.
     */
    private BufferedImage image;

    /**
     * Cor usada para desenhar o badge.
     *
     * Inicialmente definida como um tom de vermelho escuro, essa cor pode ser
     * alterada para se adequar ao estilo da interface.
     */
    private Color badgeColor = new Color(204, 0, 0);

    /**
     * Construtor padrão da classe BadgeButton.
     *
     * Inicializa o botão com uma interface personalizada, estilo transparente e
     * configurações visuais adequadas para exibir texto centralizado.
     *
     * Configurações aplicadas: - Interface personalizada com BadgeButtonUI. -
     * Fundo transparente. - Texto branco. - Margens internas definidas. - Texto
     * centralizado horizontalmente.
     *
     * Em caso de falha na configuração da interface, a exceção é capturada
     * silenciosamente.
     */
    public BadgeButton() {
        try {
            setUI(new BadgeButtonUI());
            setOpaque(false);
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(10, 11, 10, 11));
            setHorizontalTextPosition(SwingConstants.CENTER);
        } catch (Exception e) {
        }

    }

    /**
     * Cria uma imagem a partir do ícone atual do botão.
     *
     * Se o botão possuir um ícone definido, este método gera uma imagem em
     * memória baseada nesse ícone, permitindo manipulações gráficas
     * posteriores. Caso não haja ícone, a imagem é definida como nula.
     *
     * Funcionalidade: - Converte o ícone em uma BufferedImage. - Utiliza
     * Graphics2D para desenhar a imagem. - Libera os recursos gráficos após o
     * uso.
     */
    private void createImage() {
        if (getIcon() != null) {
            image = new BufferedImage(getIcon().getIconWidth(), getIcon().getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.drawImage(((ImageIcon) getIcon()).getImage(), 0, 0, null);
            g2.dispose();
        } else {
            image = null;
        }
    }

    /**
     * Define o ícone do botão e atualiza a imagem interna.
     *
     * Este método sobrescreve o comportamento padrão para garantir que a imagem
     * interna seja recriada sempre que um novo ícone for definido.
     *
     * Parâmetros: - icon: ícone a ser exibido no botão.
     */
    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        createImage();
    }

    /**
     * Classe interna responsável pela interface gráfica personalizada do
     * BadgeButton.
     *
     * Estende BasicButtonUI para permitir a renderização customizada do ícone e
     * do texto, incluindo a exibição de um badge visual sobre o botão.
     */
    private class BadgeButtonUI extends BasicButtonUI {

        /**
         * Método sobrescrito para pintar o texto do botão.
         *
         * Atualmente não implementa nenhuma lógica de pintura. Pode ser
         * personalizado futuramente para alterar a aparência do texto.
         *
         * Parâmetros: - grphcs: contexto gráfico. - jc: componente a ser
         * desenhado. - rctngl: área onde o texto deve ser renderizado. -
         * string: texto a ser exibido.
         */
        @Override
        protected void paintText(Graphics grphcs, JComponent jc, Rectangle rctngl, String string) {

        }

        /**
         * Método sobrescrito para pintar o ícone do botão com badge.
         *
         * Se houver uma imagem e texto definido, desenha um ícone com
         * sobreposição de badge. Caso contrário, utiliza o comportamento padrão
         * da superclasse.
         *
         * Parâmetros: - grphcs: contexto gráfico. - jc: componente a ser
         * desenhado. - iconRect: área onde o ícone deve ser renderizado.
         */
        @Override
        protected void paintIcon(Graphics grphcs, JComponent jc, Rectangle iconRect) {
            if (image != null && !getText().equals("")) {
                //  Custom Badge Icon
                Graphics2D g2 = (Graphics2D) grphcs.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                double size = Math.max(iconRect.getWidth(), iconRect.getHeight()) * 0.8f;
                double x = Math.min(iconRect.getX() + iconRect.getWidth() / 2, jc.getWidth() - size);
                double y = Math.max(iconRect.getY() - size / 2, 0);
                Area area = new Area(iconRect);
                area.subtract(new Area(new Ellipse2D.Double(x, y, size, size)));
                g2.setPaint(new TexturePaint(image, iconRect));
                g2.fill(area);
                String text = getText();
                createText(g2, x, y, size, text);
                g2.dispose();

            } else {
                super.paintIcon(grphcs, jc, iconRect);
            }
        }

        /**
         * Desenha o texto do badge sobre o botão.
         *
         * Cria uma forma arredondada com fundo colorido e centraliza o texto
         * dentro dela.
         *
         * Parâmetros: - g2: contexto gráfico 2D. - x: posição horizontal do
         * badge. - y: posição vertical do badge. - size: tamanho do badge. -
         * text: texto a ser exibido dentro do badge.
         */
        private void createText(Graphics2D g2, double x, double y, double size, String text) {
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D f2d = fm.getStringBounds(text, g2);
            double space = size * 0.08f;
            double width = Math.max(size - space * 2, f2d.getWidth() + 10);
            double height = size - space * 2;
            g2.setColor(badgeColor);
            g2.translate(x, y);
            g2.fill(new RoundRectangle2D.Double(space, space, width, height, height, height));
            double fx = ((width - f2d.getWidth()) / 2) + space;
            double fy = ((height - f2d.getHeight()) / 2) + space;
            g2.setColor(getForeground());
            g2.drawString(text, (int) fx, (int) fy + fm.getAscent());
        }
    }
}
