package me.microcosmos.lineBalancing;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LineBalancingSolutionMain {

	public static void main(String[] args) {

		int[][] dependency ={{1,2},{3,4},{2,5},{4,5},{5,6},{4,7},{7,8},{6,9},{9,10}};
		double[] elementWorksTimeList = {0.4,0.2,0.5,0.6,0.6,0.1,0.8,0.2,0.1,0.4};
		LineBalancingSolution a = new LineBalancingSolution(240, 480.0, dependency, elementWorksTimeList);
		double[][] PositionalWeightList = {{1.0, 5.2},{2.0, 2.6},{3.0, 3.0},{4.0,1.4}};



		a.lineBalancing();

		//a.showMatrix(a.sortedPositionalWeightList(a.positionalWeightList(a.positionalWeightMatrixIncludeMine(a.positionalWeightMatrix(a.dependencyMatrix(dependency))))));
	}
}


class LineBalancingSolution{

	int requiredUnitsPerDay;
	double workingTimePerDay;
	int[][] dependency;
	double[] elementWorksTimeList;
	double cycleTime;
	int elementWorksNumber;
	int[][] workStation;

	LineBalancingSolution(int requiredUnitsPerDay,double workingTimePerDay, int[][] dependency, double[] elementWorksTimeList){
		this.requiredUnitsPerDay = requiredUnitsPerDay;
		this.workingTimePerDay = workingTimePerDay;
		this.dependency = dependency;
		this.elementWorksTimeList = elementWorksTimeList;

		cycleTime = workingTimePerDay/requiredUnitsPerDay;
		elementWorksNumber = elementWorksTimeList.length;

	}
	/*
	 *
	 */

	public void lineBalancing(){
		int[][] dependencyMatrix = dependencyMatrix(dependency);
		int[][] positionalWeightMatrix = positionalWeightMatrix(dependencyMatrix);
		int[][] positionalWeightMatrixIncludeMine = positionalWeightMatrixIncludeMine(positionalWeightMatrix);
		double[][] positionalWeightList = positionalWeightList(positionalWeightMatrixIncludeMine);
		double[][] sortedPositionalWeightList = sortedPositionalWeightList(positionalWeightList);
		int[][] workStation = workStation(sortedPositionalWeightList);
		showMatrix(workStation);
	}

	public void showMatrix(int[][] matrix){
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[0].length;j++){
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
		}
	}

	public void showMatrix(double[][] matrix){
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[0].length;j++){
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
		}
	}

	public int[][] dependencyMatrix(int[][] denpendency){
		int[][] dependencyMatrix = new int[elementWorksNumber][elementWorksNumber];
		for(int i=0;i<dependency.length;i++){
			dependencyMatrix[dependency[i][0]-1][dependency[i][1]-1] = 1;
		}
		return dependencyMatrix;
	}

	public int[][] positionalWeightMatrix(int[][]dependencyMatrix){
		int n = dependencyMatrix.length;
		int[][] positionalWeightMatrix = new int[n][n];

		for(int i=0;i<dependencyMatrix.length;i++){
			for(int j=0;j<dependencyMatrix[0].length;j++){
				positionalWeightMatrix[i][j]=dependencyMatrix[i][j];
			}
		}

		for(int i=0; i<n; i++){
			int cnt = 1;
			List<Integer> key = new ArrayList<Integer>();
			for(int j=0; j<n; j++){
				if(positionalWeightMatrix[i][j]==1){
					key.add(j);
				}
			}
			while(cnt<n){
				int m = key.size();
				List<Integer> new1 = new ArrayList<Integer>();
				for(int l=0;l<m;l++){
					for(int w=0;w<n;w++){
						if(positionalWeightMatrix[key.get(l)][w]==1){
							new1.add(w);
						}
					}
				}
				for(int k=0; k<new1.size();k++){
					positionalWeightMatrix[i][new1.get(k)]=1;
				}
				int a = key.size();
				for(int k=0; k<a;k++){
					key.remove(0);
				}
				for(int k=0;k<new1.size();k++){
					key.add(new1.get(k));
				}

				cnt++;
			}
		}
		return positionalWeightMatrix;
	}

	public int[][] positionalWeightMatrixIncludeMine(int[][] positionalWeightMatrix){
		int[][] positionalWeightMatrixIncludeMine = new int[elementWorksNumber][elementWorksNumber];

		for(int i=0;i<positionalWeightMatrix.length;i++){
			for(int j=0;j<positionalWeightMatrix[0].length;j++){
				positionalWeightMatrixIncludeMine[i][j]=positionalWeightMatrix[i][j];
				if(i==j){
					positionalWeightMatrixIncludeMine[i][j]=1;
				}
			}
		}
		return positionalWeightMatrixIncludeMine;
	}

	public double[][] positionalWeightList(int[][] positionalWeightMatrixIncludeMine){
		double[][] positionalWeightList = new double[elementWorksNumber][2];
		for(int i=0;i<elementWorksNumber;i++){
			positionalWeightList[i][0]=i+1;
		}

		for(int i=0;i<elementWorksNumber;i++){
			for(int j=0;j<elementWorksNumber;j++){
				positionalWeightList[i][1]+=positionalWeightMatrixIncludeMine[i][j]*elementWorksTimeList[j];
			}
		}
		return positionalWeightList;
	}

	public double[][] sortedPositionalWeightList(double[][] positionalWeightList){
		double[][] sortedPositionalWeightList = new double[positionalWeightList.length][2];
		for(int i=0;i<positionalWeightList.length;i++){

			for(int j=0;j<positionalWeightList[0].length;j++){
				sortedPositionalWeightList[i][j]=positionalWeightList[i][j];
			}
		}
		for(int i=0;i<positionalWeightList.length;i++){
			for(int j=positionalWeightList.length-1;j>i;j--){
				if(sortedPositionalWeightList[j][1]>sortedPositionalWeightList[j-1][1]){
					double temp1=sortedPositionalWeightList[j][1];
					sortedPositionalWeightList[j][1]=sortedPositionalWeightList[j-1][1];
					sortedPositionalWeightList[j-1][1]=temp1;

					double temp0=sortedPositionalWeightList[j][0];
					sortedPositionalWeightList[j][0]=sortedPositionalWeightList[j-1][0];
					sortedPositionalWeightList[j-1][0]=temp0;

				}
			}
		}
		return sortedPositionalWeightList;
	}

	public int[][] workStation(double[][] sortedPositionalWeightList){

		int[][] workStation = new int[elementWorksNumber][elementWorksNumber];
		double[] cumulativeElimentWorksTimeList = new double[sortedPositionalWeightList.length];

		for(int i=0;i<elementWorksNumber;i++){
			for(int j=0;j<elementWorksNumber;j++){
				if(cumulativeElimentWorksTimeList[j]+elementWorksTimeList[(int)sortedPositionalWeightList[i][0]-1]>cycleTime){
					continue;
				}
				else{
					addTheElementWorkNumberInOperator((int)sortedPositionalWeightList[i][0], workStation, j);
					cumulativeElimentWorksTimeList[j]+=elementWorksTimeList[(int)sortedPositionalWeightList[i][0]-1];
					break;
				}

			}

		}
		this.workStation= workStation;
		return workStation;
	}

	public void addTheElementWorkNumberInOperator(int theElementWorkNumber, int[][] workStation, int operatorNumber){
		for(int i=0;i<workStation.length;i++){
			if(workStation[operatorNumber][i]!=0){
				continue;
			}
			else{
				workStation[operatorNumber][i]=theElementWorkNumber;
				break;
			}
		}
	}
}
