/**
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */
package com.todoroo.astrid.api;

import org.tasks.injection.Injector;

/**
 * Common interface for Astrids filter-exposers to provide their FilterListitem instances.
 *
 * @author Arne Jans
 */
public interface AstridFilterExposer {
    public FilterListItem[] getFilters(Injector injector);
}
