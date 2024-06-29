import ObjectComputer from "./ObjectRenderer.jsx";

export default function GOval({oval}) {

    const width = oval.dimension.width < 0 ? 24 : oval.dimension.width;
    const height = oval.dimension.height < 0 ? 24 : oval.dimension.height;

    const style = {
        position: "absolute",
        width: `${width}px`,
        height: `${height}px`,
        backgroundColor: oval.color,
        top: oval.position.height,
        left: oval.position.width,
        borderRadius: "50%"
    }

    return <div style={style}>
        {oval.childrens.map(children => {
            return (<ObjectComputer key={children.uuid} object={children} />)
        })}
    </div>
}