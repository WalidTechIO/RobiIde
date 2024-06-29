import ObjectComputer from "./ObjectRenderer.jsx";

export default function Rect({rectangle, images}) {

    const width = rectangle.dimension.width < 0 ? 24 : rectangle.dimension.width;
    const height = rectangle.dimension.height < 0 ? 24 : rectangle.dimension.height;

    const style = {
        position: "absolute",
        width: `${width}px`,
        height: `${height}px`,
        backgroundColor: rectangle.color,
        top: rectangle.position.height,
        left: rectangle.position.width
    }

    return <div style={style}>
        {rectangle.childrens.map(children => {
            return (<ObjectComputer key={children.uuid} object={children} images={images}/>)
        })}
    </div>
}