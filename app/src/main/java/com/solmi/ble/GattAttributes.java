/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.solmi.ble;

/**
 * This class includes a small subset of standard GATT attributes for
 * demonstration purposes.
 */
public class GattAttributes {

	/**
	 * TELIT custom service 1.0 version UUID
	 */
	public static String UUID_TELIT_CUSTOM_SERVICE_V1 = "53544D54-4552-494F-5345-525631303030";
	/**
	 * TELIT 데이터 송신 custom service 1.0 version UUID
	 */
	public static String UUID_TELIT_UART_DATA_TX_V1 = "53544F55-4152-5449-4E20-205630303031";
	/**
	 * TELIT 데이터 수신 custom service 1.0 version UUID
	 */
	public static String UUID_TELIT_UART_DATA_RX_V1 = "53544F55-4152-5449-4E20-205630303031";
	/**
	 * TELIT custom service 2.0 version UUID
	 */
	public static String UUID_TELIT_CUSTOM_SERVICE_V2 = "0000FEFB-0000-1000-8000-00805F9B34FB";
	/**
	 * TELIT 데이터 송신 custom service 2.0 version UUID
	 */
	public static String UUID_TELIT_UART_DATA_TX_V2 = "00000001-0000-1000-8000-008025000000";
	/**
	 * TELIT 데이터 수신 custom service 2.0 version UUID
	 */
	public static String UUID_TELIT_UART_DATA_RX_V2 = "00000002-0000-1000-8000-008025000000";
	/**
	 * TELIT Credit 송신 custom service 2.0 version UUID
	 */
	public static String UUID_TELIT_UART_CREDITS_TX_V2 = "00000003-0000-1000-8000-008025000000";
	/**
	 * TELIT Credit 수신 custom service 2.0 version UUID
	 */
	public static String UUID_TELIT_UART_CREDITS_RX_V2 = "00000004-0000-1000-8000-008025000000";
}
