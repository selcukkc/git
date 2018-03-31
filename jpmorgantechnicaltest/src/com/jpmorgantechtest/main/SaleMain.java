package com.jpmorgantechtest.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jpmorgantechtest.model.SaleDetail;
import com.jpmorgantechtest.model.SaleDetailOperation;

public class SaleMain {

	public static Integer count = 0;
	public static Map<Integer, Object> transactionMap = new HashMap<Integer, Object>();
	public static Map<String, List<SaleDetail>> saleDetailMap = new HashMap<String, List<SaleDetail>>();

	private static final Integer LOG_LIMIT = 10;
	private static final Integer TRANSACTION_STOP_LIMIT = 50;
	private static final String LOG_LIMIT_TYPE = "L";
	private static final String TRANSACTION_STOP_LIMIT_TYPE = "T";

	private static void incrementCounter() {
		count = count + 1;
	}

	//Log all the transactions
	private static void addTransactionDetails(Object saleObject) {
		transactionMap.put(count, saleObject);

	}

	//check if transactions reache the limits
	private static Boolean isTransactionReachTheLimit(String limitType) {
		if (limitType.equals(LOG_LIMIT_TYPE) && count % LOG_LIMIT == 0) {
			return true;
		}

		if (limitType.equals(TRANSACTION_STOP_LIMIT_TYPE) && count >= TRANSACTION_STOP_LIMIT) {
			return true;
		}

		return false;
	}

	//reporting the number of sales for each type
	private static void reportingTheNumberOfSales() {
		for (Entry<String, List<SaleDetail>> entry : saleDetailMap.entrySet()) {
			Double totalValue = 0.0;
			Integer i = 0;
			String key = entry.getKey();
			List<SaleDetail> value = entry.getValue();
			for (SaleDetail saleDetail : value) {
				i = i+1;
				totalValue = totalValue + saleDetail.getPrice();
			}
			System.out.println("The amount of type:" + key + " is "+i+" and  total price is:" + totalValue);
		}

	}
//first giving the information which our application is pausing then reporting the number of sales
	private static void reportOfTheAdjustments() {
		System.out.println("Application is pausing...");
		if(count == TRANSACTION_STOP_LIMIT) {
			reportingTheNumberOfSales();
		}
		
	}

	public static Integer messageType1(SaleDetail salesDetail) {
		List<SaleDetail> saleDetails = new ArrayList<SaleDetail>();
		saleDetails.add(salesDetail);
		return messageType2(saleDetails);
	}

	public static Integer messageType2(List<SaleDetail> salesDetails) {
		
		if (isTransactionReachTheLimit(LOG_LIMIT_TYPE)) {
			reportingTheNumberOfSales();
		}
		
		if (isTransactionReachTheLimit(TRANSACTION_STOP_LIMIT_TYPE)) {
			reportOfTheAdjustments();
			return count;
		}

		if (salesDetails != null) {
			for (SaleDetail saleDetail : salesDetails) {
				List<SaleDetail> mySaleDetails = new ArrayList<SaleDetail>();
				mySaleDetails = saleDetailMap.get(saleDetail.getType()) == null ? new ArrayList<SaleDetail>()
						: saleDetailMap.get(saleDetail.getType());
				mySaleDetails.add(saleDetail);
				saleDetailMap.put(saleDetail.getType(), mySaleDetails);
			}
		}
		addTransactionDetails(salesDetails);
		incrementCounter();
		return count;
	}

	public static Integer messageType3(List<SaleDetailOperation> salesDetailOperations) {

		if (isTransactionReachTheLimit(LOG_LIMIT_TYPE)) {
			reportingTheNumberOfSales();
		}

		if (isTransactionReachTheLimit(TRANSACTION_STOP_LIMIT_TYPE)) {
			reportOfTheAdjustments();
			return count;
		}

		if (salesDetailOperations != null) {
			for (SaleDetailOperation saleDetailOperation : salesDetailOperations) {
				List<SaleDetail> mySaleDetails = new ArrayList<SaleDetail>();
				mySaleDetails = saleDetailMap.get(saleDetailOperation.getType());
				if (mySaleDetails == null) {
					continue;
				}
				for (SaleDetail saleDetail : mySaleDetails) {
					switch (saleDetailOperation.getOperator()) {
					case "1":
						// Addition
						saleDetail.setPrice(saleDetail.getPrice() + saleDetailOperation.getPrice());
						break;
					case "2":
						// Substraction
						saleDetail.setPrice(saleDetail.getPrice() - saleDetailOperation.getPrice());
						break;
					case "3":
						// Multiplication
						saleDetail.setPrice(saleDetail.getPrice() * saleDetailOperation.getPrice());
						break;
					}
				}
				saleDetailMap.put(saleDetailOperation.getType(), mySaleDetails);
			}
		}
		addTransactionDetails(salesDetailOperations);
		incrementCounter();
		return count;
	}

	public static void main(String[] args) {
/*
		//MessageType1
		SaleDetail saleDetail = new SaleDetail();
		saleDetail.setType("apple0");
		saleDetail.setCount(1);
		saleDetail.setPrice(10.0);
		messageType1(saleDetail);
		
		SaleDetail saleDetail1 = new SaleDetail();
		saleDetail1.setType("apple1");
		saleDetail1.setCount(1);
		saleDetail1.setPrice(20.0);
		messageType1(saleDetail1);
*/
		
/*
		//MessageType2
		List<SaleDetail> saleDetails = new ArrayList<SaleDetail>();
		for (int i = 0; i < 51; i++) {
			SaleDetail saleDetail = new SaleDetail();
			saleDetail.setType("apple"+i % 10);
			saleDetail.setCount(i);
			saleDetail.setPrice(10.0+i);
			saleDetails.add(saleDetail);
		}
		messageType2(saleDetails);
*/
/*
		//MessageType3
		List<SaleDetailOperation> salesDetailOperations = new ArrayList<SaleDetailOperation>();
		SaleDetailOperation salesDetailOperation = new SaleDetailOperation();
		
		salesDetailOperation.setType("apple0");
		salesDetailOperation.setPrice(12.0);
		salesDetailOperation.setOperator("1");
		
		salesDetailOperations.add(salesDetailOperation);
		messageType3(salesDetailOperations);
*/	
		return;
	}

}
