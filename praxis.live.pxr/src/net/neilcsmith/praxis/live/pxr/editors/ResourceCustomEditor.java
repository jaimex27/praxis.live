/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Neil C Smith.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 *
 *
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package net.neilcsmith.praxis.live.pxr.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.neilcsmith.praxis.core.types.PResource;
import net.neilcsmith.praxis.core.types.PString;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.filesystems.FileChooserBuilder;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
class ResourceCustomEditor extends javax.swing.JPanel
        implements VetoableChangeListener, DocumentListener {

    private final URI base;
    private URI current;
    private final PropertyEnv env;
    private boolean dialogSetting;
    private final ResourceEditor editor;

    /** Creates new form ResourceCustomEditor */
    ResourceCustomEditor(ResourceEditor editor, URI base, URI current, PropertyEnv env) {
        initComponents();
        if (current != null) {
            uriField.setText(current.toString());
        }
        this.base = base;
        this.current = current;
        this.env = env;
        this.editor = editor;
//        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addVetoableChangeListener(this);
        uriField.getDocument().addDocumentListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        uriField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();

        uriField.setText(org.openide.util.NbBundle.getMessage(ResourceCustomEditor.class, "ResourceCustomEditor.uriField.text")); // NOI18N

        browseButton.setText(org.openide.util.NbBundle.getMessage(ResourceCustomEditor.class, "ResourceCustomEditor.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(uriField, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(browseButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uriField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addContainerGap(259, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        File loc = null;
        if (current != null && "file".equals(current.getScheme())) {
            try {
                loc = new File(current);
            } catch (Exception e) {
            }
        }
        if (loc == null && base != null && "file".equals(base.getScheme())) {
            try {
                loc = new File(base);
            } catch (Exception e) {
            }
        }
        FileChooserBuilder dlgBld = new FileChooserBuilder(ResourceCustomEditor.class);
        if (loc != null) {
            dlgBld.setDefaultWorkingDirectory(loc).forceUseOfDefaultWorkingDirectory(true);
        }
        dlgBld.setTitle("Choose File").setApproveText("OK");
        File file = dlgBld.showOpenDialog();
        if (file != null) {
            current = file.toURI();
            dialogSetting = true;
            uriField.setText(current.toString());
//            env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
            env.setState(PropertyEnv.STATE_VALID);
            editor.setValue(PResource.valueOf(current));
            dialogSetting = false;
        }
    }//GEN-LAST:event_browseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JTextField uriField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void insertUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update(e);
    }

    private void update(DocumentEvent e) {
        if (dialogSetting) {
            return;
        }
        String txt = uriField.getText();
        if (txt.isEmpty()) {
            current = null;
            env.setState(PropertyEnv.STATE_VALID);
            editor.setValue(PString.EMPTY);
        } else {
            try {
                current = PResource.valueOf(uriField.getText()).value();
                env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
            } catch (Exception ex) {
                env.setState(PropertyEnv.STATE_INVALID);
            }
        }


    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (current == null) {
            editor.setValue(PString.EMPTY);
        } else {
            editor.setValue(PResource.valueOf(current));
        }

    }
}
