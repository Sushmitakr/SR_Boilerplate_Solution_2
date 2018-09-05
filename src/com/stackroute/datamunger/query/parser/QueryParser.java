package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.List;

/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	//public String[] splitString = queryString.toLowerCase().split(" ");
	

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		String baseQuery = queryString.split("where|order by|group by")[0].trim();
		 String fileName = baseQuery.split("from")[1].trim().split("\\s+")[0].trim();
		 
		queryParameter.setQueryString(queryString);
		queryParameter.setBaseQuery(baseQuery);
		queryParameter.setFileName(fileName);
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));
		
		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	
	
	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {
		
		List<String> listStringOrder = new ArrayList<String>();
		if(queryString.contains("order by")) {
			 listStringOrder.add(queryString.split("order by")[1].trim());
		}
	            
	       return listStringOrder;
	}
	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */

	public List<String> getGroupByFields(String queryString) {
		
		 List<String> groupByFields = new ArrayList<String>();
		 
		if(queryString.contains("group by") && !queryString.contains("order by")) {
			groupByFields.add(queryString.split("group by")[1].trim());
			return groupByFields;
		}
		else if(queryString.contains("group by") && queryString.contains("order by")) {
			groupByFields.add(queryString.split("order by")[0].trim().split("group by")[1].trim());
			return groupByFields;
		}
		return null;
	    }

	
	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */

	public List<String> getFields(String queryString){
		 List<String> fields = new ArrayList<String>();
		 
		 String[] splitString = queryString.split("select")[1].trim().split("from")[0].trim().split(",");
		  for(int i=0; i<splitString.length; i++) {
			  fields.add(splitString[i].trim());
		  }
		 
		 //fields.add("winner");
		 //fields.add("season");
		 //fields.add("team2");
		 return fields;
	}
	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */
	
	public String[] getConditions(String queryString) {
	       String[] conditions= {""};
	       if( queryString.contains("where") )
	       {
	       int j=0;
	       if( queryString.contains("='") )
	       {
	           queryString=queryString.replace("='", "= '");
	           queryString=queryString.replaceAll("'", "");
	           
	       }
	       String[] conditionsPartQuery=queryString.split(" ");
	       
	       for(int i=1;i<conditionsPartQuery.length-1;i++)
	       {
	           if( conditionsPartQuery[i].equals("=") | conditionsPartQuery[i].equals(">") | conditionsPartQuery[i].equals("<") | conditionsPartQuery[i].equals(">=") | conditionsPartQuery[i].equals("=<") | conditionsPartQuery[i].equals("=>") | conditionsPartQuery[i].equals("<=") )
	           {
	               j++;
	           }
	       
	       }
	       
	       int l=0;
	       conditions= new String[j];
	       for(int i=1;i<conditionsPartQuery.length-1;i++)
	       {
	           
	           if( conditionsPartQuery[i].equals("=") | conditionsPartQuery[i].equals(">") | conditionsPartQuery[i].equals("<") | conditionsPartQuery[i].equals(">=") | conditionsPartQuery[i].equals("=<") | conditionsPartQuery[i].equals("=>") | conditionsPartQuery[i].equals("<=") )
	           {
	               
	               conditions[l]=conditionsPartQuery[i-1]+" "+conditionsPartQuery[i]+" "+conditionsPartQuery[i+1];
	               /*if( conditions[l].contains("=") )
	               {
	                   conditions[l]=conditions[l].replace("= '", "='");
	               }*/
	               
	               l++;
	           }
	       
	       }
	      return conditions;
	       }
	       else
	       {
	           return conditions;
	       }
	   }

	    
		      
		    
	
		 public List<Restriction> getRestrictions(String queryString)
		    {
		        List<Restriction> restrictionsList=new ArrayList<Restriction>();
		           String[] conditionArray=getConditions(queryString);
		           for(int i=0;i<conditionArray.length;i++)
		           {
		               String[] tempConditionArray=conditionArray[i].split(" ");
		               if(tempConditionArray.length==3)
		               {
		               Restriction restriction=new Restriction(tempConditionArray[0], tempConditionArray[2], tempConditionArray[1]);
		               restrictionsList.add(restriction);
		               }
		               else
		               {
		                   restrictionsList=null;
		               }
		              
		           }
		            return restrictionsList;
		        }
			

	
	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */

		 private List<String> getLogicalOperators(String queryString) {
		       
		       List<String> logicalCondition = new ArrayList<>();
		       if (queryString.contains(" where ")) {
		           String[] str = queryString.toLowerCase().split(" ");
		           int l = 0;
		           for (int i = 0; i < str.length; i++) {
		               // System.out.println(str[i]);
		               if (str[i].equals("and") | str[i].equals("or")) {
		                   l++;
		               }
		           }
		           String[] logic = new String[l];        
		           l = 0;
		           for (int i = 0; i < str.length; i++) {
		               // System.out.println(str[i]);
		               if (str[i].equals("and") | str[i].equals("or")) {
		                   logic[l] = str[i];
		                   //System.out.println(logic[l]);
		                   l++;
		               }
		           }
		           for(String s:logic) {
		               logicalCondition.add(s);
		           }
		           return logicalCondition;
		       }
		       else {
		           return null;
		       }
		   }
		 /* List<String> logicalNew = new ArrayList<String>();
		 
		 String splitStrings = queryString.split("where")[1].trim();
		
		 if(splitStrings.contains("and")|| splitStrings.contains("or"))
				 {int l=0;
			 for(int i = 0; i<splitStrings.length() ; i++) {
				 if(splitStrings.equals("and")) {
					String str = splitStrings;
					logicalNew.add(str);
					l++;
				 }else if(splitStrings.equals("or")) {
					 String str = splitStrings;
					 logicalNew.add(str);
					 l++;
				 }
			 }
		
		 String[] logical=new String[l];
		 int l=0;
			if(splitStrings.contains("and") && splitStrings.contains("or")) {
			
				for(int i = 0; i<splitStrings.length() ; i++) {
					
					if(splitStrings.equals("and") && splitStrings.equals("or")) {
						
						for(int j = 0; j<splitStrings.length(); j++) {
							logical[l]	= splitStrings[i];
							l++;
						}
					}
				}
				
			}
			logicalNew.add(splitStrings);
			 System.out.println(logicalNew);
			 
		//}
		
		return null;
				 }
	}*/
	
	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString){
		
		 if (hasAggregateFunctions(queryString)) {
	           queryString = queryString.trim();
	           String aggregateFunctions[] = queryString.split("from")[0].split("select")[1].split(",");
	           int size = aggregateFunctions.length;
	           String aggregate;
	           String function;
	           String aggregateField;
	           List<AggregateFunction> agregateFunctionList = new ArrayList<AggregateFunction>();
	           AggregateFunction agregateFunction;
	           for (int i = 0; i < size; i++) {
	               aggregate = aggregateFunctions[i].trim();
	               if (aggregate.contains("(")) {
	                   function = aggregate.split("\\(")[0].trim();
	                   aggregateField = aggregate.split("\\(")[1].trim().split("\\)")[0];
	                   agregateFunction = new AggregateFunction(aggregateField,function);
	                   //agregateFunction.setField(aggregateField);
	                   //agregateFunction.setFunction(function);
	                   agregateFunctionList.add(agregateFunction);
	               }
	           }
	           return agregateFunctionList;
	       }
	       return null;
	   }
	    private boolean hasAggregateFunctions(String queryString) {
	       if (queryString.contains("sum") || queryString.contains("min") || queryString.contains("max")
	               || queryString.contains("avg") || queryString.contains("count")) {
	          // queryParameter.setQuery_Type("AGGREGATE_QUERY");
	           return true;
	       }
	       return false;
	   }
		
	

}