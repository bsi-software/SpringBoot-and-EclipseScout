package org.eclipse.scout.tasks.scout.ui.user;

import java.util.Locale;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link ILocaleLookupService}</h3>
 *
 * @author mzi
 */
@TunnelToServer
public interface ILocaleLookupService extends ILookupService<Locale> {
}
