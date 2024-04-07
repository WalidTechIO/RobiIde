import ObjectComputer from "./ObjectComputer.jsx";

export default function Rect({rectangle}) {

    if(rectangle.dimension.width < 0){
        rectangle.dimension.width = 24
    }
    if (rectangle.dimension.height < 0) {
        rectangle.dimension.height = 24
    }

    const style = {
        position: "absolute",
        width: `${rectangle.dimension.width}px`,
        height: `${rectangle.dimension.height}px`,
        backgroundColor: rectangle.color,
        top: rectangle.position.height,
        left: rectangle.position.width
    }

    return <div style={style}>
        {rectangle.childrens.map(children => {
            return (<ObjectComputer key={children} object={children}/>)
        })}
    </div>
}