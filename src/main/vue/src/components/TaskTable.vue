<template>
    <div class="btn-container">
        <v-btn variant="flat" color="primary" rounded="10" size="small" class="delete-btn" @click="showDeleteCheckboxes">{{ (checkDeletedOn)? "취소" : "삭제" }}</v-btn>
        <v-btn variant="flat" color="secondary" size="small" v-show="checkDeletedOn" @click="deleteBtnHandler">삭제하기</v-btn>
        <div class="datepicker-btn">
            <v-btn flat >
            <v-icon icon="mdi-triangle-small-down" size="x-large"></v-icon>
            {{ selectedDate.toLocaleDateString() }} (오늘)
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
        </div>
        <transition>
            <div class="remain-tasks" v-show="!checkDeletedOn">
                {{tasks.length === 0 ? '오늘 뭘 해볼까요?' : `${completedTasksNum} / ${tasks.length}` }}
            </div>
        </transition>
    </div>
    
    <v-table class="task-table" density="comfortable">
        <thead>
            <tr>
                <td v-show="checkDeletedOn">
                    <input type="checkbox" id="deletingAll" @input="deletingAll">
                    <label for="deletingAll"></label>
                </td>
                <td>시간</td>
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
                    달성
                </td>
                <td>
                    <span>메모</span>
                </td>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(task, index) in tasks" :key="index" class="task">
                <td v-show="checkDeletedOn">
                    <input type="checkbox" :id="`deleting${index}`" v-model="deleting[index]">
                    <label :for="`deleting${index}`"></label>
                </td>
                <td class="date" id="datepicker">
                    {{ getTime(task.startedAt) }}
                    <v-menu 
                        v-model="task.timepickerOpened" 
                        location="bottom center" 
                        origin="top center" 
                        activator="parent"
                        :close-on-content-click="false"
                        >
                        <timepicker 
                            :value="task.startedAt"
                            @submit="changeTime($event, index)"/>
                    </v-menu>
                </td>
                <td class="what-to-do">
                    <img :src="(task.category?.imageUrl) ? `/image/category/beer.png` : undefined" :alt="task.category?.name">
                    {{ task.category?.name }}
                    <v-menu v-model="task.todoMenuOpened" location="bottom center" origin="top center"
                        activator="parent"
                        :close-on-content-click="false">
                        <to-do-selector :opened="todoSelector"
                            @todoSelected="changeToDo($event, index)"/>
                    </v-menu>
                </td>
                <td>
                    <input type="checkbox" name="" :id="`checkbox${index}`" :checked="(task.completedAt !== null)" @input="completeCheckHandler(index)">
                    <label :for="`checkbox${index}`"></label>
                </td>
                <td class="memo">
                    <input type="text" v-model="task.memo" @change="updateMemo(index)">
                </td>
            </tr>
            <tr>
                <td colspan="5">
                    <button @click="addEmptyTask"></button>
                </td>
            </tr>
        </tbody>
    </v-table>
</template>

<script setup lang="ts">
import { watch, ref, reactive, computed } from 'vue'
import DateSelector from '@/components/DateSelector.vue';
import ToDoSelector from '@/components/TodoSelector.vue';
import { Category } from '@/components/TodoSelector.vue';
import Datepicker from '@/components/Datepicker.vue';
import Timepicker, { SelectedTime } from '@/components/Timepicker.vue';

const todoSelector = ref(false);
const dateSelector = ref(false);
const currentDateSelector = ref(false);

const deleting:boolean[] = reactive([]);

interface Task {
    id: number | null,
    startedAt: Date,
    completedAt: Date | null,
    category: Category | null,
    memo: string | null,
    timepickerOpened: boolean,
    todoMenuOpened: boolean,
}

interface SelectedCategory{
    parent:Category,
    child:Category
}

const currDate = new Date();
currDate.setHours(0,0,0,0);

const selectedDate = ref(currDate);

const checkDeletedOn = ref(false);

const tasks: Array<Task> = reactive([]);

function getTime(datetime: Date) {
    const isAm = datetime.getHours() >= 12 ? false : true;
    const hour = datetime.getHours() > 12 ?  datetime.getHours() - 1 : datetime.getHours();
    const hourStr = hour < 10 ? `0${hour}` : hour.toString();
    const minuteStr = datetime.getMinutes() < 10 ? `0${datetime.getMinutes()}` : datetime.getMinutes().toString();
    return `${isAm ? '오전' : '오후'} ${hourStr}:${minuteStr}`;
}

function dateSelectedHandler(date: Date) {
    console.log(date);
    const newTask: Task = {
        id: null,
        startedAt: date,
        completedAt: null,
        category: null,
        memo: null,
        timepickerOpened: false,
        todoMenuOpened: false,
    }
    if(areDatesWithoutTimeEqual(date, selectedDate.value)){
        tasks.push(newTask);
        tasks.sort(taskComparator);
    }
    else{
        console.log(date);
        submitDateHandler({
            formattedDate : "",
            dateInfo:{
                year: date.getFullYear(),    
                month: date.getMonth() + 1, 
                day: date.getDate(),
            }
        })
        tasks.push(newTask);
    }
    deleting.push(false);   
}

function updateMemo(index: number) {
    console.log(tasks[index].memo);
}

function closeDatepicker(){
    currentDateSelector.value=false;
}

function getTasks(date: Date){
    const url = new URL('http://localhost:3000/api/task');
    url.searchParams.append('date', date.toISOString());
    fetch(url)
    .then(res=>{
        if (!res.ok){
            throw new Error();
        }
        else{
            return res.json();
        }
    })
    .then((data: Array<Task>) =>{
        tasks.length = 0;
        deleting.length = 0;
        data.forEach(task => {
            task.startedAt = new Date(task.startedAt);
            tasks.push(task);
            deleting.push(false);
        });
    })
    .catch(err=>{
        alert('할 일을 불러오는 과정에서 오류가 발생했어요...');
    })
}

function todoSelectHandler(currSelected: SelectedCategory) {
    todoSelector.value = false;
    tasks[0].category = currSelected.child;
    const option = {
        method: 'post',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(tasks[0]),
    }
    fetch('/api/task', option)
    .then(res=>console.log(res));
}

function submitDateHandler(data:{
        formattedDate : string, 
        dateInfo : {year:number, month:number, day:number}
    }){
    const date = new Date();
    const dateInfo = data.dateInfo;
    date.setFullYear(dateInfo.year, dateInfo.month-1, dateInfo.day);
    tasks.length = 0;
    getTasks(date);
    selectedDate.value = date;
}

function changeTime(param:SelectedTime,index:number){
    let hours = (param.isAm || param.hour === 12) ? param.hour : param.hour + 12;
    tasks[index].startedAt.setHours(hours, param.minute);
    tasks[index].timepickerOpened = false;
}

function changeToDo(param:SelectedCategory, index:number){
    tasks[index].todoMenuOpened=false;
    tasks[index].category = param.child;
}

function taskComparator (a:Task,b:Task){
    if(a.startedAt < b.startedAt){
        return -1;
    }
    else if(a.startedAt > b.startedAt){
        return 1;
    }
    else{
        if (a.category === null){
            return -1;
        }
        else{
            return 1;
        }
    }
}

function areDatesWithoutTimeEqual(dateA:Date, dateB: Date){
    return dateA.toDateString() === dateB.toDateString();
}

function addEmptyTask(){
    const startedAt = new Date(selectedDate.value);
    const emptyTask: Task = {
        id: null,
        startedAt: startedAt,
        completedAt : null,
        category: null,
        memo: null,
        timepickerOpened: false,
        todoMenuOpened: false,
    }
    tasks.push(emptyTask);
    deleting.push(false);
}

function showDeleteCheckboxes(){
    checkDeletedOn.value = !checkDeletedOn.value;
}

const completedTasksNum = computed(()=>{
    let count = 0;
    tasks.forEach(task=>{
        if (task.completedAt !== null) count++;
    })
    return count;
})

function completeCheckHandler(index:number){
    const completedAt = tasks[index].completedAt ? null : new Date();
    tasks[index].completedAt = completedAt;
    updateTask(index);
}

function updateTask(index:number){
    const option = {
        method:'PUT',
        body:JSON.stringify(tasks[index]),
        headers: {
            "Content-Type": "application/json",
        }
    }
    fetch('api/task',option)
    .then(res=>{
        if(!res.ok){
            alert('업데이트에 실패했어요...');
        }
    })
}

function deletingAll(event:Event) {
    const element = event.target as HTMLInputElement;
    const value = element.checked;
    for (let i = 0; i < deleting.length; i++){
        deleting[i] = value;
    }
}

function deleteBtnHandler() {
    if(!confirm(`정말 ${deleting.length} 개의 할 일을 삭제하시겠습니까?`)){
        return;
    }
    const deletedIds = deleting.map((el,idx)=>{
        if (el){
            return tasks[idx].id;
        }
    })

    const form = new FormData();
    form.append('ids', deletedIds.toString())
    const option = {
        method:'DELETE',
        body: form,
    }
    fetch('api/task', option)
    .then(res=>{
        if(!res.ok){
            alert('삭제과정에서 오류가 발생했어요.');
            return;
        }
        
        getTasks(selectedDate.value);
    })

}

// watch(todoSelector,()=>{
//     if(todoSelector.value && tasks.length === 0){
//         todoSelector.value = false;
//     }
// })

</script>

<style lang="scss" scoped>
.task-table{
    border-collapse:collapse;
    min-height:500px;

    thead{ 
        border-style:none;
        border-spacing: 0px;

        tr {
            td:nth-child(2){
                border-top-left-radius: 5px;
            }
            td:last-child{
                border-top-right-radius: 5px;
            }
        }
    }


    td{
        border:0.3px solid rgba(128, 128, 128, 0.209);
        transition: all 1s;
        &:first-child{
            width:20px;
            margin:0 !important;
            padding:none !important;
        }
        &:nth-child(2){
            width:80px;
        }
        &:nth-child(3){
            width:80px;
        }
        &:nth-child(4){
            width:80px;
        }
        &:nth-child(5){
            width:80px;
        }
    }
    .memo{
        padding:5px 5px;
        input{
            width:100%;
            text-align: center;
            height:100%;
        }
        input:focus{
            outline: 1px solid rgb(0, 41, 247);
            background:skyblue;
        }
    }
}
@media (max-width: 576px) {
    *{
        font-size:12px;
    }
}


.delete-btn{
    margin-right:10px;
}

.btn-container{
    display:flex;
    align-items:center;
    margin-bottom: 10px;
}

.datepicker-btn{
    margin: 0 auto;
}

.selectedDate {
    font-weight:var(--bold);
}

.what-to-do{
    img{
        height:20px;
    }
}

tbody > tr > td:first-child:hover {
    // transform: scale(1.05);
    // border: 10px solid blue;
    background-color:#b1e0e8f4;
    transition: background-color 0.3s;
}


tbody > tr > td:nth-child(2):hover {
    // transform: scale(1.05);
    // border: 10px solid blue;
    background-color:#ffb0eeca;
    transition: background-color 0.3s;
}

tbody > tr > td:nth-child(3){
    position:relative;
}

tbody > tr > td:nth-child(3):hover {
    // transform: scale(1.05);
    // border: 10px solid blue;
    background-color:#b6e3c4ba;
    transition: background-color 0.3s;
    // position:relative;
}

tbody > tr > td:nth-child(4):hover {
    // transform: scale(1.05);
    // border: 10px solid blue;
    background-color:#0000001a;
    transition: background-color 0.3s;
}

input[type='checkbox']{
    appearance: none;
    border: 1.5px solid var(--true-blue);
    width: 15px;
    height: 15px;
    border-radius: 4px;
    z-index:999;
}

input[type='checkbox'] + label {
    display:none;
    position:absolute;
}

input[type='checkbox']:checked + label{
    display: inline-block;
    background-image:url('@/assets/image/check.svg');
    content: "";
    width:30px;
    height:30px;
    transform:translate(calc(-50% - 5px),calc(-50% + 3px));
    // z-index:0;
}

tbody > tr:last-child{

}

tbody > tr:last-child > td > button{
    width: 30px;
    height: 30px;
    margin: auto auto;
    background-image:url('@/assets/image/circle-plus.svg')
}

.remain-tasks{
    font-size:12px;
}

.v-enter-active,
.v-leave-active {
  transition: all 0.5s ease;
}

.v-enter-from,
.v-leave-to {
    transform:translateX(30px);
    opacity: 0;
}
</style>
