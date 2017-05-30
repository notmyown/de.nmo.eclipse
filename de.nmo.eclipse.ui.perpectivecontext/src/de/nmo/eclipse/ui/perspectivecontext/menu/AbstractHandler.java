/**
 *
 ******************************************************************
 *     Copyright VW AG, Germany     *
 ******************************************************************
 *
 ******************************************************************
 *Administrative Information (automatically filled in by MKS)
 ******************************************************************
 *
 * $ProjectName: $
 * $Author: $
 * $Date: $
 * $Name:  $
 * $ProjectRevision: 1.81 $
 * $Revision: 1.141 $
 * $Source: central.mak $
 ******************************************************************
**/
package de.nmo.eclipse.ui.perspectivecontext.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

/**
 * Stub for IHandler to prevent unused code
 *
 */
public abstract class AbstractHandler implements IHandler {

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core.commands.IHandlerListener)
   */
  @Override
  public void addHandlerListener(IHandlerListener handlerListener) {
    //not implemented - sub classes may implement
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.commands.IHandler#dispose()
   */
  @Override
  public void dispose() {
    // not implemented - sub classes may implement
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.commands.IHandler#isEnabled()
   */
  @Override
  public boolean isEnabled() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.commands.IHandler#isHandled()
   */
  @Override
  public boolean isHandled() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.core.commands.IHandlerListener)
   */
  @Override
  public void removeHandlerListener(IHandlerListener handlerListener) {
    //not implemented - sub classes may implement
  }

}
