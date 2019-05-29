COUNT=$1

JAVA_EXE=java
MPJ_HOME=
GIT_FOR_WINDOWS_HOME=

DATA_GEN="${JAVA_EXE} -cp lab1.jar ru.generator.Main"
PARALLEL_RUN="${JAVA_EXE} -jar ${MPJ_HOME}/lib/starter.jar ru.lab2.Main -cp lab1.jar runtime.starter.MPJRun"
SEQUENTIAL_RUN="${JAVA_EXE} -cp lab1.jar ru.lab1.Main"


export MPJ_HOME

if [ -f "answer.csv" ]; then
	rm "answer.csv"
fi

for i in $(seq 1 $COUNT); do
	DATASET="run-$i"
	echo "${DATA_GEN} -d ${i}0 -p ${i}00 -ap ${i}00 -apl ${i} -aph ${i}0"
	${DATA_GEN} -d 5 -p 100 -ap ${i}0000 -apl 3 -aph 15

	${PARALLEL_RUN} > temp
	cat temp

	echo "---"
	echo "Thread times: "

	cat temp | grep -v MPJ | sed -r "s#^.*Took: ##g" > temp
	cat temp
	

	echo "---"
	echo "Thread max time: "
	cat temp | ${GIT_FOR_WINDOWS_HOME}/usr/bin/sort -n | tail -1 > temp
	cat temp
	PAR_TIME="$(cat temp)"

	#####

	echo "---"
	echo "Squential run: "
	${SEQUENTIAL_RUN} > temp
	cat temp
	SEQ_TIME="$(cat temp)"

	echo "${DATASET},${PAR_TIME},${SEQ_TIME}" >> answer.csv
done

