import axios from 'axios';
import { BASE_URL } from '@/compositions/global.js'

const { URL, token } = BASE_URL();

export const writeRoute = async (title, routeStartDate, routeEndDate, routeContent, routePid, routeImage) => {
	const req = {
		"title" : title, 
		"routeStartDate" : routeStartDate,
		"routeEndDate" : routeEndDate, 
		"routeContent" : routeContent, 
		"routePid" : routePid
	}

	const headers = {
		'Authorization' : token
	}

	let frm = new FormData();
	frm.append("image", routeImage);
	frm.append("request", JSON.stringify(req));

	await axios({
	method: 'post',
	url: URL + 'route/write',
	headers: headers,
	data : frm
	})
	.then((res) => {
		console.log(res.data);
		return res
	})
	.catch((err) => {
		alert('글 작성을 실패하였습니다.')
		return err
	})
}

export const writeVisit = async (rid, title, visitPid, visitCost, visitTime, visitContent, visitImage1, visitImage2, visitImage3) => {
	const req = {
		"title" : title, 
		"visitPid" : visitPid,
		"visitCost" : visitCost, 
		"visitTime" : visitTime, 
		"visitContent" : visitContent,
	}
	
	const headers = {
		'Authorization' : localStorage.getItem('Authorization')
	}

	console.log(visitImage1);
	console.log(visitImage2);
	console.log(visitImage3);
	
	let frm = new FormData();
	frm.append("image1", visitImage1);
	frm.append("image2", visitImage2);
	frm.append("image3", visitImage3);
	frm.append("rid", rid);
	frm.append("request", JSON.stringify(req));
	await axios({
		method: 'post',
		headers: headers,
		url: URL + 'visit/write',
		data : frm,
		// data: {
		// 	request : JSON.stringify(req),
		// 	rid : rid, 
		// 	image1 : visitImage1, 
		// 	image2 : visitImage2, 
		// 	image3 : visitImage3
		// },
		
	})
	.then((res) => {
		console.log(res.data);
		return res
	})
	.catch((err) => {
		alert(err)
		return err
	})
}
