/*
 Copyright 2014 David Salter

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.davidsalter.bootstrapcdn;

import java.util.Collections;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.*;
import org.openide.util.*;
import org.openide.util.Lookup;

/**
 * Main implementation of a NetBeans "insert code" plugin to add Bootstrap CDN links into a HTML
 * file.  
 * 
 * Install the nbm, and then right click in a .xhtml file and choose "insert code..." and
 * then "Bootstreap CDN...".  A dialog will be displayed showing version 3 and upwards
 * of Bootstrap.  Select the version to link to and click OK.
 * 
 * @author david@developinjava.com 31-03-2014
 */
public class BootstrapCdnCodeGenerator implements CodeGenerator {

    JTextComponent textComp;

    /**
     *
     * @param context containing JTextComponent and possibly other items
     * registered by {@link CodeGeneratorContextProvider}
     */
    private BootstrapCdnCodeGenerator(Lookup context) { // Good practice is not to save Lookup outside ctor
        textComp = context.lookup(JTextComponent.class);
    }

    @MimeRegistration(mimeType = "text/html", service = CodeGenerator.Factory.class)
    public static class Factory implements CodeGenerator.Factory {

        public List<? extends CodeGenerator> create(Lookup context) {
            return Collections.singletonList(new BootstrapCdnCodeGenerator(context));
        }
    }

    /**
     * The name which will be inserted inside Insert Code dialog
     */
    @Override
    public String getDisplayName() {
        return "Bootstrap CDN...";
    }

    /**
     * This will be invoked when user chooses this Generator from Insert Code
     * dialog
     */
    @Override
    public void invoke() {
        BootstrapVersionPanel form = new BootstrapVersionPanel();
        DialogDescriptor dd = new DialogDescriptor(form, "Bootstrap Version");
        Object result = DialogDisplayer.getDefault().notify(dd);
        if (result == NotifyDescriptor.OK_OPTION) {
            String version;
            version = form.getSelectedVersion();
            try {
                Caret caret = textComp.getCaret();
                int dot = caret.getDot();
                textComp.getDocument().insertString(dot, "<link href=\"//netdna.bootstrapcdn.com/bootstrap/" + version + "/css/bootstrap.min.css\" rel=\"stylesheet\"></link>", null);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
