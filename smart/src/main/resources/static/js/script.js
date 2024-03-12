console.log("this is script file")


// const toggleSidebar =()=>{

//     if($('.sidebar').is(".visible")){
//         //true
//         $(".sidebar").css("display", "none");
//         $(".content").css("margin-left"," 0%");

//     }
//     else{
//         //false

//         $(".sidebar").css("display", "block");
//         $(".content").css("margin-left"," 20%");
//     }
   
// };

function toggleSidebar(){
    const sidebar = document.getElementsByClassName("sidebar")[0];
    const content = document.getElementsByClassName("content")[0];

    if(window.getComputedStyle(sidebar).display=="none"){
        sidebar.style.display="block";
        content.style.marginLeft="20%";
    }
    else{

        sidebar.style.display="none";
        content.style.marginLeft="0%";

    }
}

const search =() =>{
   let query = $("#search-input").val();
   if(query == ""){
    $(".search-result").hide();

   }
   else{
    console.log(query);

    //sending request to server
    let url = `https://smartcontact-production-7ba7.up.railway.app/search/${query}`;
    fetch(url).then((response)=>{
        return response.json();
    }).then ((data)=>{
        
        let text =`<div class='list-group'>`
           data.forEach((contact) => {
             text += `<a href='/user/${contact.cId}/contact' class ='list-group-item list-group-item-action'>${contact.name}</a>`
           });
        text+= `</div>`
        $(".search-result").html(text);
        $(".search-result").show();

    });
   
   }
};

