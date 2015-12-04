/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bremersee.comparator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.bremersee.comparator.model.ComparatorItem;

/**
 * <p>
 * Class that implements {@link ComparatorItemDeserializer} and
 * {@link ComparatorItemSerializer}.
 * </p>
 * <p>
 * The serialized form of a single {@link ComparatorItem} looks like:
 * 
 * <pre>
 * fieldName,asc  or  fieldName,desc
 * </pre>
 * 
 * </p>
 * <p>
 * The serialized form of a concatenated {@link ComparatorItem} looks like:<br/>
 * 
 * <pre>
 * fieldName1,asc|fieldName2,desc|fieldName3,asc
 * </pre>
 * 
 * </p>
 * <p>
 * The deserializer can read these forms and creates the corresponding
 * {@link ComparatorItem}.
 * </p>
 * 
 * @author Christian Bremer
 */
public class ComparatorItemTransformerImpl implements ComparatorItemTransformer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bremersee.comparator.ComparatorItemSerializer#toString(org.bremersee.
	 * comparator.model.ComparatorItem, boolean, java.lang.String)
	 */
	@Override
	public String toString(ComparatorItem comparatorItem, boolean urlEncode, String charset) {

		ComparatorItem tmp = comparatorItem;
		StringBuilder sb = new StringBuilder();
		String str = null;
		while ((str = toString(tmp)) != null) {
			if (sb.length() > 0) {
				sb.append('|');
			}
			sb.append(str);
			tmp = tmp.getNextComparatorItem();
		}
		if (!urlEncode) {
			return sb.toString();
		}
		try {
			if (StringUtils.isBlank(charset)) {
				charset = "utf-8";
			}
			return URLEncoder.encode(sb.toString(), charset);

		} catch (UnsupportedEncodingException e) {
			throw new ComparatorItemTransformerException(e);
		}
	}

	private String toString(ComparatorItem comparatorItem) {
		if (comparatorItem == null) {
			return null;
		}
		if (StringUtils.isBlank(comparatorItem.getField())) {
			return toString(comparatorItem.getNextComparatorItem());
		}
		final String order;
		if (comparatorItem.isAsc()) {
			order = "asc";
		} else {
			order = "desc";
		}
		return comparatorItem.getField() + "," + order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bremersee.comparator.ComparatorItemDeserializer#fromString(java.lang.
	 * String, boolean, java.lang.String)
	 */
	@Override
	public ComparatorItem fromString(String serializedComparatorItem, boolean isUrlEncoded, String charset) {

		if (StringUtils.isBlank(serializedComparatorItem)) {
			return null;
		}

		if (isUrlEncoded) {
			try {
				if (StringUtils.isBlank(charset)) {
					charset = "utf-8";
				}
				serializedComparatorItem = URLDecoder.decode(serializedComparatorItem, charset);

			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		int index = serializedComparatorItem.indexOf('|');
		if (index < 0) {
			return fromString(serializedComparatorItem);

		} else {
			String param1 = serializedComparatorItem.substring(0, index);
			String param2 = serializedComparatorItem.substring(index + 1);

			ComparatorItem item1 = fromString(param1);
			ComparatorItem item2 = fromString(param2, false, charset);
			if (item2 != null) {
				item1.setNextComparatorItem(item2);
			}

			return item1;
		}
	}

	private ComparatorItem fromString(String serializedComparatorItem) {

		if (StringUtils.isBlank(serializedComparatorItem)) {
			return null;
		}

		int index = serializedComparatorItem.indexOf(',');
		if (index < 0) {
			return new ComparatorItem(serializedComparatorItem);
		} else {
			String field = serializedComparatorItem.substring(0, index);
			String tmp = serializedComparatorItem.substring(index + 1);
			boolean asc = !"desc".equalsIgnoreCase(tmp);
			return new ComparatorItem(field, asc);
		}
	}

}
