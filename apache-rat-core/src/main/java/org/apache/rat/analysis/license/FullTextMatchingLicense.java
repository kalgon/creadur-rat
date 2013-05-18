/*
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 */ 
package org.apache.rat.analysis.license;

import java.util.Locale;

import org.apache.rat.analysis.IHeaderMatcher;
import org.apache.rat.analysis.RatHeaderAnalysisException;
import org.apache.rat.api.Document;
import org.apache.rat.api.MetaData.Datum;

/**
 * Accumulates all letters and numbers contained inside the header and
 * compares it to the full text of a given license (after reducing it
 * to letters and numbers as well).
 *
 * <p>The text comparison is case insensitive but assumes only
 * characters in the US-ASCII charset are being matched.</p>
 *
 * @since Rat 0.9
 */
public class FullTextMatchingLicense extends BaseLicense
    implements IHeaderMatcher {

    private String fullText;

    private final StringBuilder buffer = new StringBuilder();

    public FullTextMatchingLicense() {
    }

    protected FullTextMatchingLicense(Datum licenseFamilyCategory,
                                      Datum licenseFamilyName,
                                      String notes,
                                      String fullText) {
        super(licenseFamilyCategory, licenseFamilyName, notes);
        setFullText(fullText);
    }

    public final void setFullText(String text) {
        fullText = prune(text).toLowerCase(Locale.ENGLISH);
    }

    public final boolean hasFullText() {
        return fullText != null;
    }

    // TODO this is still quite inefficient if the match does not occur near the start of the buffer
    // see RAT-138
    public boolean match(Document subject, String line) throws RatHeaderAnalysisException {
        buffer.append(prune(line).toLowerCase(Locale.ENGLISH));
        if (buffer.toString().contains(fullText)) {
            reportOnLicense(subject);
            return true;
        }
        return false;
    }

    public void reset() {
        buffer.setLength(0);
    }
}