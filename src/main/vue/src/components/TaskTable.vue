<template>
    <v-btn flat >
        <v-icon icon="mdi-triangle-small-down" size="x-large"></v-icon>
        오늘
        <v-menu v-model="currentDateSelector" 
            location="bottom center" 
            origin="top center"
            activator="parent"
            :close-on-content-click="false">
            <datepicker 
                @dateSelected="dateSelectedHandler" 
                @closeDatepicker="closeDatepicker"
                @submit-date="submitDateHandler"
            />
        </v-menu>
    </v-btn>
    <div class="selectedDate">{{ selectedDate.toLocaleDateString() }}</div>
    <v-table>
        <thead>
            <tr>
                <td>
                    <v-btn flat>
                        <v-icon icon="mdi-triangle-small-down" size="x-large"></v-icon>
                        시간
                        <v-menu transition="scale-transition" v-model="dateSelector" location="bottom center"
                            origin="top center" activator="parent">
                            <date-selector 
                                @dateSelected="dateSelectedHandler"/>
                        </v-menu>
                    </v-btn>
                </td>
                <td>
                    <v-btn flat>
                        <v-icon icon="mdi-triangle-small-down" size="x-large"></v-icon>
                        할일
                        <v-menu v-model="todoSelector" location="bottom center" origin="top center" activator="parent"
                        :close-on-content-click="false">
                            <to-do-selector :opened="todoSelector"
                            @todoSelected="todoSelectHandler"/>
                        </v-menu>
                    </v-btn>
                </td>
                <td>
                    <span>Memo</span>
                </td>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(task, index) in tasks" :key="index" class="task">
                <td class="date" id="datepicker">
                    {{ getTime(task.datetime) }}
                    <v-menu 
                        v-model="task.timepickerOpened" 
                        location="bottom center" 
                        origin="top center" 
                        activator="parent"
                        :close-on-content-click="false"
                        >
                        <timepicker 
                            :value="task.datetime"
                            @submit="timeSubmitHandler($event, index)"/>
                    </v-menu>
                </td>
                <td class="What-to-do">
                    <img :src="(task.imgUrl) ? `/image/category/${task.imgUrl}` : undefined" :alt="task.category?.child.name">
                    {{ task.category?.child.name }}
                    <v-menu v-model="task.todoMenuOpened" location="bottom center" origin="top center"
                        activator="parent"
                        :close-on-content-click="false">
                        <to-do-selector :opened="todoSelector"
                            @todoSelected="todoSelectHandler"/>
                    </v-menu>
                </td>
                <td class="Memo">
                    <input type="text" v-model="task.memo" @change="updateMemo(index)">
                </td>
            </tr>
        </tbody>
    </v-table>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import DateSelector from '@/components/DateSelector.vue';
import ToDoSelector from '@/components/TodoSelector.vue';
import { Category } from '@/components/TodoSelector.vue';
import Datepicker from '@/components/Datepicker.vue';
import Timepicker, { SelectedTime } from '@/components/Timepicker.vue';

const todoSelector = ref(true);
const dateSelector = ref(true);
const currentDateSelector = ref(true);

interface Task {
    id: number | null,
    datetime: Date,
    imgUrl: string | null,
    category: SelectedCategory | null,
    memo: string | null,
    timepickerOpened: boolean,
    todoMenuOpened: boolean,
}

interface SelectedCategory{
    parent:Category,
    child:Category
}

const selectedDate = ref(new Date());

const tasks: Array<Task> = reactive([]);

function getTime(datetime: Date) {

    const localDatetimeString = datetime.toLocaleTimeString();
    if (localDatetimeString.length === 10) {
        return localDatetimeString.slice(0, 7);
    }
    else {
        return localDatetimeString.slice(0, 8);
    }
}

function dateSelectedHandler(date: Date) {
    const newTask: Task = {
        id: null,
        datetime: date,
        imgUrl: null,
        category: null,
        memo: null,
        timepickerOpened: false,
        todoMenuOpened: false,
    }
    tasks.push(newTask);
}

function updateMemo(index: number) {
    console.log(tasks[index].memo);
}

function closeDatepicker(){
    currentDateSelector.value=false;
}

function todoSelectHandler(currSelected:SelectedCategory){
    todoSelector.value = false;
    tasks[0].category = currSelected;
}

function submitDateHandler(data:any){
    const date = new Date();
    const dateInfo = data.dateInfo;
    date.setFullYear(dateInfo.year, dateInfo.month-1, dateInfo.day);
    tasks.length = 0;
    selectedDate.value = date;
}

function timeSubmitHandler(param:SelectedTime,index:number){
    let hours = param.isAm ? param.hour : param.hour + 12;
    tasks[index].datetime.setHours(hours, param.minute);
    tasks[index].timepickerOpened = false;
}

</script>

<style lang="scss">
.selectedDate {
    font-weight:var(--bold);
}
</style>
