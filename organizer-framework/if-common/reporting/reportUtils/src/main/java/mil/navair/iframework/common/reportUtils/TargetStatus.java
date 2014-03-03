/*
 *  * To change this template, choose Tools | Templates
 *   * and open the template in the editor.
 *    */
package mil.navair.iframework.common.reportUtils;

/**
 * Status enum for Targets
 * @author dandrus
 */
public enum TargetStatus
{

    /**
     * Target is running (able to accept new messages)
     */
    Running,
    /**
     * Target is stopped (not receiving new messages)
     */
    Stopped,
    /**
     * Target does not exist
     */
    NonExistent;
}

