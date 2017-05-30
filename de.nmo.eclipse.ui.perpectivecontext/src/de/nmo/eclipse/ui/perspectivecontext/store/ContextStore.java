package de.nmo.eclipse.ui.perspectivecontext.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.ResourcesPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.nmo.eclipse.ui.perspectivecontext.store.model.Perspective;
import de.nmo.eclipse.ui.perspectivecontext.store.model.PerspectiveContext;
import de.nmo.eclipse.ui.perspectivecontext.utils.Utils;

/**
 * 
 *
 */
public class ContextStore {

  /**
   * @return the default configurationpath within the workspace
   *
   */
  public static File getConfigFile() {
    String ws = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    String path = ws + "/.metadata/.plugins/de.nmo.eclipse.ui.perspectivecontext";
    File f = new File(path + "/context.xml");
    return f;
  }

  /**
   * @param store the File to load from
   * @return a List of PerspectiveContext from the loaded file, may be empty when the file does not exists
   */
  public static List<PerspectiveContext> readXML(File store) {
    List<PerspectiveContext> contexts = new ArrayList<>();
    if (!store.exists()) {
      return contexts;
    }
    Document dom;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder db = dbf.newDocumentBuilder();
      dom = db.parse(store);

      Element root = dom.getDocumentElement();
      for (int i = 0; i < root.getChildNodes().getLength(); i++) {
        Node child = root.getChildNodes().item(i);
        if ("context".equals(child.getNodeName())) {
          PerspectiveContext context = new PerspectiveContext(
              child.getAttributes().getNamedItem("name").getNodeValue());
          Node defNode = child.getAttributes().getNamedItem("default");
          if (defNode != null) {
            Perspective defPerspective = Utils.getFullPerspectiveById(defNode.getNodeValue());
            if (defPerspective != null) {
              context.setDefaultperspective(defPerspective);
            }
          }
          for (int j = 0; j < child.getChildNodes().getLength(); j++) {
            Node grandchild = child.getChildNodes().item(j);
            if ("perspective".equals(grandchild.getNodeName())) {
              Perspective newPerspective = Utils.getFullPerspectiveById(grandchild.getTextContent().trim());
              context.getPerspectives().add(newPerspective);
            }
          }
          contexts.add(context);
        }
      }
    } catch (ParserConfigurationException | SAXException |

        IOException e) {
      e.printStackTrace();
    }

    return contexts;
  }

  /**
   * @param store File to store to
   * @param contexts List of Perspectivecontext to persist
   * @return
   *
   */
  public static boolean writeXML(File store, List<PerspectiveContext> contexts) {
    File storeto = store;
    if (storeto == null) {
      storeto = getConfigFile();
    }

    Document dom;
    Element e = null;

    // instance of a DocumentBuilderFactory
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      if (!storeto.exists()) {
        storeto.getParentFile().mkdirs();
        storeto.createNewFile();
      }
      DocumentBuilder db = dbf.newDocumentBuilder();
      dom = db.newDocument();
      Element rootEle = dom.createElement("contexts");
      for (PerspectiveContext pc : contexts) {
        e = dom.createElement("context");
        e.setAttribute("name", pc.getName());
        if (pc.getDefaultperspective() != null) {
          e.setAttribute("default", (pc.getDefaultperspective() != null) ? pc.getDefaultperspective().getName() : "");
        }
        rootEle.appendChild(e);
        for (Perspective p : pc.getPerspectives()) {
          Element e1 = dom.createElement("perspective");
          e1.appendChild(dom.createTextNode(p.getId()));
          e.appendChild(e1);
        }
      }
      dom.appendChild(rootEle);
      Transformer tr = TransformerFactory.newInstance().newTransformer();
      tr.setOutputProperty(OutputKeys.INDENT, "yes");
      tr.setOutputProperty(OutputKeys.METHOD, "xml");
      tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

      // send DOM to file
      tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(storeto)));

    } catch (TransformerException | IOException | ParserConfigurationException e1) {
      e1.printStackTrace();
    }
    return true;
  }

}
